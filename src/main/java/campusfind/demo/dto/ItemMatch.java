package campusfind.demo.dto;

import campusfind.demo.entity.FoundItem;
import campusfind.demo.entity.LostReport;

public class ItemMatch {

    private LostReport lostReport;
    private FoundItem foundItem;
    private String matchReason;
    private int matchScore; // Score percentage (e.g. 80%)

    public ItemMatch() {
    }

    public ItemMatch(LostReport lostReport, FoundItem foundItem, String matchReason, int matchScore) {
        this.lostReport = lostReport;
        this.foundItem = foundItem;
        this.matchReason = matchReason;
        this.matchScore = matchScore;
    }

    public LostReport getLostReport() {
        return lostReport;
    }

    public void setLostReport(LostReport lostReport) {
        this.lostReport = lostReport;
    }

    public FoundItem getFoundItem() {
        return foundItem;
    }

    public void setFoundItem(FoundItem foundItem) {
        this.foundItem = foundItem;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }
}
