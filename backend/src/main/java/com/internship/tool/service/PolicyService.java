package com.internship.tool.service;

import com.internship.tool.entity.Policy;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async; 

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service // Business logic layer
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AiServiceClient aiServiceClient;

    // ✅ CREATE policy (clear cache + send email)
    @CacheEvict(value = {"policies", "policy"}, allEntries = true)
    public Policy createPolicy(Policy policy) {

        // Save policy to DB
        Policy savedPolicy = policyRepository.save(policy);

        // Send email after creation
        emailService.sendPolicyCreatedEmail(
                "test@gmail.com", // replace later
                savedPolicy.getTitle()
        );

        generateAiAsync(savedPolicy.getId(), savedPolicy.getDescription());

        return savedPolicy;
    }

    // ✅ GET all policies (cached)
    @Cacheable(value = "policies")
    public Page<Policy> getAllPolicies(Pageable pageable) {
        System.out.println("Fetching from DB...");
        return policyRepository.findAll(pageable);
    }

    // ✅ GET policy by ID (cached)
    @Cacheable(value = "policy", key = "#id")
    public Policy getPolicyById(Long id) {
        System.out.println("Fetching from DB...");
        return policyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy not found with id: " + id));
    }

    // ✅ DELETE policy (clear cache)
    @CacheEvict(value = {"policies", "policy"}, allEntries = true)
    public void deletePolicy(Long id) {
        Policy policy = getPolicyById(id); // ensure exists
        policyRepository.delete(policy);
    }

    // ✅ NEW: Check overdue policies and send email
    public void checkOverduePolicies() {

        // Fetch all policies
        List<Policy> policies = policyRepository.findAll();

        for (Policy policy : policies) {

            // Check if due date exists AND is expired
            if (policy.getDueDate() != null &&
                policy.getDueDate().isBefore(LocalDateTime.now())) {

                // Send overdue email
                emailService.sendPolicyCreatedEmail(
                        "test@gmail.com", // replace later
                        policy.getTitle() + " is overdue"
                );
            }
        }
    }

    //  DAY 7 — ASYNC AI METHOD 
    @Async
    public void generateAiAsync(Long policyId, String input) {
        try {
            var response = aiServiceClient.generateReport(input);

            //  Handle null safely
            if (response == null || response.get("data") == null) {
                System.out.println("AI failed or returned null");
                return;
            }

            Object data = response.get("data");

            if (data == null) {
                  System.out.println("AI data missing");
              return;
            }

            String aiResult = data.toString();

            Policy policy = policyRepository.findById(policyId).orElse(null);

            if (policy != null) {
                policy.setAiReport(aiResult);
                policyRepository.save(policy);
            }

        } catch (Exception e) {
            System.out.println("Async error: " + e.getMessage());
        }
    }
}