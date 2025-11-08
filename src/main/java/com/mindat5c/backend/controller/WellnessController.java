package com.mindat5c.backend.controller;

import com.mindat5c.backend.model.CheckIn;
import com.mindat5c.backend.model.CheckInResponse;
import com.mindat5c.backend.model.Resource;
import com.mindat5c.backend.model.Stats;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WellnessController {

    private List<CheckIn> checkIns = new ArrayList<>();

    @GetMapping("/stats")
    public Stats getStats() {
        if (checkIns.isEmpty()) {
            return new Stats(0, null, null, null, null, 50);
        }

        double avgSleep = checkIns.stream().mapToInt(CheckIn::getSleep).average().orElse(0);
        double avgNutrition = checkIns.stream().mapToInt(CheckIn::getNutrition).average().orElse(0);
        double avgStress = checkIns.stream().mapToInt(CheckIn::getStress).average().orElse(0);
        double avgActivity = checkIns.stream().mapToInt(CheckIn::getActivity).average().orElse(0);

        // Community Pulse: (sleep + nutrition + (10-stress) + activity) / 4 * 10
        double pulse = (avgSleep + avgNutrition + (10 - avgStress) + avgActivity) / 4 * 10;
        pulse = Math.max(0, Math.min(100, pulse));

        return new Stats(
            checkIns.size(),
            Math.round(avgSleep * 10.0) / 10.0,
            Math.round(avgNutrition * 10.0) / 10.0,
            Math.round(avgStress * 10.0) / 10.0,
            Math.round(avgActivity * 10.0) / 10.0,
            (int) Math.round(pulse)
        );
    }

    @PostMapping("/seed")
    public Map<String, Object> seedData() {
        // Generate 15 diverse check-ins
        Random random = new Random();

        int[][] samples = {
            {7, 6, 8, 5},  // Moderate
            {4, 5, 9, 3},  // High stress, poor sleep
            {8, 7, 4, 6},  // Good overall
            {6, 8, 6, 7},  // Good nutrition
            {3, 4, 9, 2},  // Very stressed
            {9, 8, 3, 8},  // Excellent
            {5, 6, 7, 4},  // Average
            {8, 5, 5, 7},  // Good sleep
            {6, 7, 8, 5},  // Moderate stress
            {4, 6, 9, 3},  // High stress
            {7, 8, 4, 6},  // Low stress
            {5, 5, 6, 5},  // Average all
            {8, 7, 5, 7},  // Good overall
            {6, 6, 7, 6},  // Moderate
            {7, 7, 6, 6}   // Balanced
        };

        for (int[] sample : samples) {
            CheckIn checkIn = new CheckIn();
            checkIn.setSleep(sample[0]);
            checkIn.setNutrition(sample[1]);
            checkIn.setStress(sample[2]);
            checkIn.setActivity(sample[3]);
            checkIn.setTimestamp(Instant.now().minusSeconds(random.nextInt(604800)).toString()); // Random time in last week
            checkIns.add(checkIn);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ok", true);
        response.put("message", "15 check-ins added");
        return response;
    }

    @PostMapping("/checkin")
    public CheckInResponse submitCheckIn(@RequestBody CheckIn checkIn) {
        checkIn.setTimestamp(Instant.now().toString());
        checkIns.add(checkIn);

        List<Resource> resources = generateResources(checkIn);

        return new CheckInResponse(
            true,
            "Check-in recorded. Here are some resources for you.",
            resources
        );
    }

    private List<Resource> generateResources(CheckIn checkIn) {
        List<Resource> resources = new ArrayList<>();

        // Priority resources based on thresholds
        if (checkIn.getStress() >= 7 || checkIn.getSleep() <= 4) {
            String reason = checkIn.getStress() >= 7 ? "High stress detected" : "Poor sleep detected";
            resources.add(new Resource(
                "Student Wellness Center",
                "https://students.dartmouth.edu/wellness-center",
                true,
                reason
            ));
            resources.add(new Resource(
                "Dick's House Counseling",
                "https://students.dartmouth.edu/health-service/counseling",
                true,
                null
            ));
        }

        if (checkIn.getNutrition() <= 5) {
            resources.add(new Resource(
                "Nutrition Counseling",
                "https://students.dartmouth.edu/health-service/nutrition",
                true,
                "Poor nutrition detected"
            ));
            resources.add(new Resource(
                "FOCO/DDS Healthy Menus",
                "https://nutrition.dartmouth.edu/menus",
                false,
                null
            ));
        }

        if (checkIn.getActivity() <= 4) {
            resources.add(new Resource(
                "Alumni Gym",
                "https://recreation.dartmouth.edu/facilities/alumni-gym",
                false,
                null
            ));
            resources.add(new Resource(
                "DOC Outdoor Trips",
                "https://outdoors.dartmouth.edu/trips",
                false,
                null
            ));
        }

        // Always show general resources
        resources.add(new Resource(
            "Campus Events (Collis)",
            "https://collis.dartmouth.edu/events",
            false,
            null
        ));

        return resources;
    }
}
