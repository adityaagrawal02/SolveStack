// =============================================
// FILE 2: ui/ChallengeRepository.java
// FINAL VERSION
// REAL SUBMISSION MAPPING
// =============================================

package ui;

import dao.ChallengeDAO;
import dao.SubmissionDAO;
import models.Challenge;
import models.Company;
import models.Submission;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRepository {

    private static ChallengeRepository instance;

    private final ChallengeDAO challengeDAO =
            new ChallengeDAO();

    private final SubmissionDAO submissionDAO =
            new SubmissionDAO();

    private ChallengeRepository() {
    }

    public static ChallengeRepository getInstance() {

        if (instance == null) {
            instance = new ChallengeRepository();
        }

        return instance;
    }

    /* ==========================================
       GET ALL CHALLENGES
       ========================================== */

    public List<Challenge> getAllChallenges() {

        List<Challenge> list =
                new ArrayList<>();

        List<String[]> rows =
                challengeDAO.getAllOpenChallenges();

        for (String[] row : rows) {

            try {

                Company company =
                        new Company(
                                row[2], // company_id
                                row[2],
                                "company@solvestack.com",
                                "hidden",
                                "Company",
                                "Technology",
                                "REG001"
                        );

                Challenge c =
                        new Challenge(
                                row[0], // challenge_id
                                row[1], // title
                                "Live challenge from database",
                                company,
                                Double.parseDouble(row[3]),
                                30
                        );

                list.add(c);

            } catch (Exception ignored) {
            }
        }

        return list;
    }

    /* ==========================================
       ADD CHALLENGE
       ========================================== */

    public void addChallenge(
            Challenge challenge) {

        if (challenge == null)
            return;

        try {

            challengeDAO.addChallenge(
                    challenge.getTitle(),
                    challenge.getDescription(),
                    challenge.getPostedBy()
                            .getUserId(),
                    challenge.getPrizeAmount()
            );

        } catch (Exception ignored) {
        }
    }

    /* ==========================================
       GET CHALLENGE BY ID
       ========================================== */

    public Challenge getChallengeById(
            String id) {

        for (Challenge c :
                getAllChallenges()) {

            if (c.getChallengeId()
                    .equalsIgnoreCase(id)) {

                return c;
            }
        }

        return null;
    }

    /* ==========================================
       GET ALL SUBMISSIONS
       ========================================== */

    public List<Submission> getAllSubmissions() {

        List<Submission> list =
                new ArrayList<>();

        List<String[]> rows =
                submissionDAO.getAllSubmissions();

        for (String[] row : rows) {

            try {

            /*
             row indexes:
             0 submission_id
             1 challenge_id
             2 developer_id
             3 solution_summary
             4 status
             5 score
             6 submitted_at
            */

                Submission s =
                        new Submission(
                                row[0],   // submission id
                                row[2],   // developer username/id
                                row[1],   // challenge id
                                row[3]    // solution summary
                        );

                list.add(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<Submission> getSubmissionsByDeveloper(
            String developerId) {

        List<Submission> list =
                new ArrayList<>();

        List<String[]> rows =
                submissionDAO.getSubmissionsByDeveloper(
                        developerId
                );

        for (String[] row : rows) {

            try {

            /*
             DAO returns:
             0 submission_id
             1 challenge_id
             2 solution_summary
             3 status
             4 score
            */

                Submission s =
                        new Submission(
                                row[0],
                                developerId,
                                row[1],
                                row[2]
                        );

                list.add(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public int getSubmissionCountByDeveloper(
            String developerId) {

        return getSubmissionsByDeveloper(
                developerId
        ).size();
    }

    /* ==========================================
       GET SUBMISSION BY ID
       ========================================== */

    public Submission getSubmissionById(
            String subId) {

        for (Submission s :
                getAllSubmissions()) {

            if (s.getSubmissionId()
                    .equalsIgnoreCase(subId)) {

                return s;
            }
        }

        return null;
    }
}