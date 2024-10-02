package com.github.uber_eat_cloneapi1.controller.driver;

import com.github.uber_eat_cloneapi1.dto.request.StatusUpdatedDTO;
import org.springframework.web.bind.annotation.*;

public class Driver {

    @GetMapping("/drivers/available")
    public String getAvailableDrivers() {
        return "List of available drivers.";
    }

    @GetMapping("/drivers/{driverId}")
    public String getDriverDetails(@PathVariable String driverId) {
        return "Details of driver with ID: " + driverId;
    }

    @PostMapping("/drivers/{driverId}/update-status")
    public String updateDriverStatus(@PathVariable String driverId, @RequestBody StatusUpdatedDTO statusUpdateDTO) {
        return "Driver status updated for ID: " + driverId;
    }


}
