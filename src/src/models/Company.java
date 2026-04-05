package models;

/*
 * ============================================================
 *  SolveStack – Open Innovation Collaboration Platform
 *  File   : models.Company.java
 *  Package: models
 *  Role   : Represents a corporate entity that posts challenges
 *           and reviews developer submissions.
 *
 *  OOP Principles Applied:
 *  ─────────────────────────────────────────────────────────
 *   Inheritance    – Extends models.User; inherits login, logout,
 *                     updateProfile, and all getters.
 *   Encapsulation  – models.Company-specific fields are private;
 *                     accessed through controlled methods.
 *   Polymorphism   – Overrides getRole() and
 *                     displayRoleDashboard() from models.User.
 *   Abstraction    – Implements all abstract methods from models.User.
 * ============================================================
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company extends User {

    private String companyName;
    private String industry;
    private String website;
    private String registrationNumber;
    private final List<Challenge> postedChallenges;

    public Company(String userId,
                   String username,
                   String email,
                   String password,
                   String companyName,
                   String industry,
                   String registrationNumber) {

        super(userId, username, email, password);

        if (companyName == null || companyName.isBlank())
            throw new IllegalArgumentException("companyName cannot be blank.");
        if (industry == null || industry.isBlank())
            throw new IllegalArgumentException("industry cannot be blank.");
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("registrationNumber cannot be blank.");

        this.companyName = companyName;
        this.industry = industry;
        this.registrationNumber = registrationNumber;
        this.website = "";
        this.postedChallenges = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Company";
    }

    @Override
    protected void displayRoleDashboard() {
        long open = postedChallenges.stream()
                .filter(c -> c.getStatus() == Challenge.Status.OPEN).count();
        long closed = postedChallenges.stream()
                .filter(c -> c.getStatus() == Challenge.Status.CLOSED).count();

        System.out.println("Company: " + companyName);
        System.out.println("Industry: " + industry);
        System.out.println("Website: " + (website.isBlank() ? "N/A" : website));
        System.out.println("Total Challenges: " + postedChallenges.size());
        System.out.println("Open: " + open);
        System.out.println("Closed: " + closed);
    }

    @Override
    public void onAccountRemoved() {
        for (Challenge challenge : postedChallenges) {
            if (challenge.getStatus() == Challenge.Status.OPEN) {
                challenge.closeChallenge();
            }
        }
    }

    public Challenge createChallenge(String title,
                                     String description,
                                     double prizeAmount,
                                     int durationDays) {

        if (!isLoggedIn() || !isVerified()) return null;

        String challengeId = "CH-" + getUserId() + "-" + (postedChallenges.size() + 1);

        Challenge challenge = new Challenge(
                challengeId, title, description, this, prizeAmount, durationDays
        );

        postedChallenges.add(challenge);
        return challenge;
    }

    public void closeChallenge(String challengeId) {
        Challenge challenge = findChallenge(challengeId);
        if (challenge != null) {
            challenge.closeChallenge();
        }
    }

    public void viewSubmissions(String challengeId) {
        Challenge challenge = findChallenge(challengeId);
        if (challenge == null) return;

        List<Submission> submissions = challenge.getSubmissions();

        for (Submission s : submissions) {
            System.out.println(s.getSubmissionId() + " | " + s.getDeveloperUsername());
        }
    }

    private Challenge findChallenge(String challengeId) {
        for (Challenge c : postedChallenges) {
            if (c.getChallengeId().equals(challengeId)) {
                return c;
            }
        }
        return null;
    }

    public String getCompanyName() { return companyName; }
    public String getIndustry() { return industry; }
    public String getWebsite() { return website; }
    public String getRegistrationNumber() { return registrationNumber; }

    public List<Challenge> getPostedChallenges() {
        return Collections.unmodifiableList(postedChallenges);
    }

    public void setWebsite(String website) {
        if (website != null && !website.isBlank()) {
            this.website = website;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Company{id='%s', username='%s', company='%s'}",
                getUserId(), getUsername(), companyName
        );
    }
}