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


@Service
public class Spongebot {
    private Boolean isWorking = false;
    private SpongebotService sbotService;
     private PostService postService;




    @Autowired
    public Spongebot(SpongebotService sbotService, PostService postService){
        this.sbotService = sbotService;
        this.postService = postService;
    }


    public void run(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Optional<SpongebotParameters> spongebotParams = sbotService.getSpongeBotParameters();
                    if (spongebotParams.isPresent()) {
                        if (spongebotParams.get().getBotWorking()) {
                            if (!isWorking) {
                                isWorking = true;
                                initSBOT(spongebotParams.get().getBotInterval());
                            }

                        }
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

    private void initSBOT(Integer interval){
        PostService postService = new PostService();
        ScheduledExecutorService botExecutor = Executors.newScheduledThreadPool(1);
        botExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Optional<SpongebotParameters> optional = sbotService.getSpongeBotParameters();
                    if (optional.isPresent()) {
                        SpongebotParameters spongebotParameters = optional.get();
                        Long startTime = (CurrentTime.getCurrentTime());
                        if (spongebotParameters.getPopularCounter() > 0) {
                            PopularPostsRetreiver(spongebotParameters);
                        }
                        if (spongebotParameters.getAllCounter() > 0) {
                            AllPostsRetreiver(spongebotParameters);
                        }
                        if (spongebotParameters.getFeaturedCounter() > 0){
                            FeaturedPostsRetreiver(spongebotParameters);
                        }

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
