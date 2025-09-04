package com.daribear.PrefyBackend.Actuator;

import com.daribear.PrefyBackend.Activity.UserActivity.UserActivity;
import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.security.Principal;
import java.util.Optional;

/**
 * An endpoint for monitoring System CPU usage, where you want to return the cpu usage
 *
 */
@Component
@Endpoint(id = "systemCpu")
public class SystemCpuUsage {

    /**
     * Returns the current percentage usage of the cpu under load.
     * @return (double) CPU usage as a percentage
     */
    @ReadOperation
    public Double getSystemCPU(){
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return osBean.getCpuLoad() * 100;
    }
}
