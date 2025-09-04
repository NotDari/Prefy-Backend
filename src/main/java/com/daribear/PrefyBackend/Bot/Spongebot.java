package com.daribear.PrefyBackend.Bot;

import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostService;
import com.daribear.PrefyBackend.Utils.CurrentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Spongebot is a bot which automates voting on posts to simulate traffic.
 *
 * This bot can vote on posts with specific tags or in general with a specific intervals.
 *
 *
 */
@Service
public class Spongebot {
    private Boolean isWorking = false;
    private SpongebotService sbotService;
     private PostService postService;



    //Dependancy injection
    @Autowired
    public Spongebot(SpongebotService sbotService, PostService postService){
        this.sbotService = sbotService;
        this.postService = postService;
    }

    /**
     * Run the bot loop by started a loop that checks the parameters every 2 seconds.
     * Starts/Stops the bot depending on what the parameters state.
     */
    public void run(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Optional<SpongebotParameters> spongebotParams = sbotService.getSpongeBotParameters();
                    if (spongebotParams.isPresent()) {
                        //Check if bot should be started
                        if (spongebotParams.get().getBotWorking()) {
                            if (!isWorking) {
                                isWorking = true;
                                initSBOT(spongebotParams.get().getBotInterval());
                            }

                        }
                        //Checks if bot should end
                        if (!spongebotParams.get().getBotOn()){
                            executorService.shutdownNow();
                        }
                    } else {
                        executorService.shutdownNow();
                    }
                } catch (Exception e){
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Initialises the automated voting executor.
     * Executes at fixed intervals to avoid voting all at one instance.
     * @param interval interval in seconds to run the bot
     */
    private void initSBOT(Integer interval){
        PostService postService = new PostService();
        ScheduledExecutorService botExecutor = Executors.newScheduledThreadPool(1);
        botExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get spongebt paramters
                    Optional<SpongebotParameters> optional = sbotService.getSpongeBotParameters();
                    if (optional.isPresent()) {
                        SpongebotParameters spongebotParameters = optional.get();
                        Long startTime = (CurrentTime.getCurrentTime());
                        //Vote on popular posts
                        if (spongebotParameters.getPopularCounter() > 0) {
                            PopularPostsRetreiver(spongebotParameters);
                        }
                        //Vote on default posts
                        if (spongebotParameters.getAllCounter() > 0) {
                            AllPostsRetreiver(spongebotParameters);
                        }
                        //Vote on featured posts
                        if (spongebotParameters.getFeaturedCounter() > 0){
                            FeaturedPostsRetreiver(spongebotParameters);
                        }
                        //Decrement the amount of time for the bot to stay alive
                        spongebotParameters.setStopCounter(spongebotParameters.getStopCounter() - Math.toIntExact(((new Date()).getTime() / 1000) - (startTime / 1000) + spongebotParameters.getBotInterval()));
                        if (spongebotParameters.getStopCounter() <= 0) {
                            botExecutor.shutdownNow();
                            isWorking = false;
                            spongebotParameters.setBotWorking(false);
                            spongebotParameters.setStopCounter(0);
                            sbotService.updateSpongeBotParameters(spongebotParameters);
                        } else {
                            sbotService.updateSpongeBotParameters(spongebotParameters);
                        }
                        //Check if bot is off
                        if (!optional.get().getBotOn()) {
                            botExecutor.shutdownNow();
                            isWorking = false;
                        } else if (!optional.get().getBotWorking()) {
                            botExecutor.shutdownNow();
                            isWorking = false;
                        }

                    }

                } catch (Exception e){
                }

            }
        }, 0, interval, TimeUnit.SECONDS);
    }


    /**
     * Retrieves and votes on the popular posts.
     * @param spongebotParameters bot parameters which include number of popular posts to vote on.
     */
    private void PopularPostsRetreiver(SpongebotParameters spongebotParameters){
        Pageable pageable = PageRequest.of(0, spongebotParameters.getPopularCounter().intValue());
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<Post>> postOptional = postService.getBotPosts(pageable, true, false, spongebotParameters.getBothFeaPop());
        if (postOptional.isPresent()){
            List<Post> postList = postOptional.get();
            for (Post post : postList){
                int random = (int )(Math.random() * 2 + 1);
                if (random == 1){
                    post.setLeftVotes(post.getLeftVotes() + 1);
                } else if (random == 2){
                    post.setRightVotes(post.getRightVotes() + 1);
                }
                post.setAllVotes(post.getAllVotes() + 1);
            }
            postService.savePostList(postList);
        }
    }


    /**
     * Retrieves and votes on the posts that aren't popular and aren't featured
     * @param spongebotParameters bot parameters which include number of posts to vote on.
     */
    private void AllPostsRetreiver(SpongebotParameters spongebotParameters){
        Pageable pageable = PageRequest.of(0, spongebotParameters.getAllCounter().intValue());
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<Post>> postOptional = postService.getBotPosts(pageable, false, false, spongebotParameters.getBothFeaPop());
        if (postOptional.isPresent()){
            List<Post> postList = postOptional.get();
            for (Post post : postList){
                int random = (int )(Math.random() * 2 + 1);
                if (random == 1){
                    post.setLeftVotes(post.getLeftVotes() + 1);
                } else if (random == 2){
                    post.setRightVotes(post.getRightVotes() + 1);
                }
                post.setAllVotes(post.getAllVotes() + 1);
            }
            postService.savePostList(postList);
        }
    }

    /**
     * Retrieves and votes on the featured posts.
     * @param spongebotParameters bot parameters which include number of features posts to vote on.
     */
    private void FeaturedPostsRetreiver(SpongebotParameters spongebotParameters){
        Pageable pageable = PageRequest.of(0, spongebotParameters.getFeaturedCounter().intValue());
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        pageable.getSortOr(sort);
        Optional<List<Post>> postOptional = postService.getBotPosts(pageable, false , true, spongebotParameters.getBothFeaPop());
        if (postOptional.isPresent()){
            List<Post> postList = postOptional.get();
            for (Post post : postList){
                int random = (int )(Math.random() * 2 + 1);
                if (random == 1){
                    post.setLeftVotes(post.getLeftVotes() + 1);
                } else if (random == 2){
                    post.setRightVotes(post.getRightVotes() + 1);
                }
                post.setAllVotes(post.getAllVotes() + 1);
            }
            postService.savePostList(postList);
        }
    }

    /**
     * Shuts down the spongebot gracefully.
     */
    @PreDestroy
    public void shutdown(){
        Optional<SpongebotParameters> spongebotParams = sbotService.getSpongeBotParameters();
        if (spongebotParams.isPresent()) {
            if (spongebotParams.get().getBotOn()) {
                spongebotParams.get().setBotOn(false);
                sbotService.updateSpongeBotParameters(spongebotParams.get());
            }
        }
    }
}
