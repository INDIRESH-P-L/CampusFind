package campusfind.demo.dto;

public class DashboardStats {

    private long totalUsers;
    private long studentCount;
    private long staffCount;
    private long pendingFoundItems;   // AVAILABLE
    private long claimedFoundItems;   // CLAIMED
    private long returnedFoundItems;  // RETURNED
    private long openLostReports;     // OPEN
    private long resolvedLostReports; // RESOLVED

    public DashboardStats() {
    }

    public DashboardStats(long totalUsers, long studentCount, long staffCount,
                          long pendingFoundItems, long claimedFoundItems, long returnedFoundItems,
                          long openLostReports, long resolvedLostReports) {
        this.totalUsers = totalUsers;
        this.studentCount = studentCount;
        this.staffCount = staffCount;
        this.pendingFoundItems = pendingFoundItems;
        this.claimedFoundItems = claimedFoundItems;
        this.returnedFoundItems = returnedFoundItems;
        this.openLostReports = openLostReports;
        this.resolvedLostReports = resolvedLostReports;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public long getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(long staffCount) {
        this.staffCount = staffCount;
    }

    public long getPendingFoundItems() {
        return pendingFoundItems;
    }

    public void setPendingFoundItems(long pendingFoundItems) {
        this.pendingFoundItems = pendingFoundItems;
    }

    public long getClaimedFoundItems() {
        return claimedFoundItems;
    }

    public void setClaimedFoundItems(long claimedFoundItems) {
        this.claimedFoundItems = claimedFoundItems;
    }

    public long getReturnedFoundItems() {
        return returnedFoundItems;
    }

    public void setReturnedFoundItems(long returnedFoundItems) {
        this.returnedFoundItems = returnedFoundItems;
    }

    public long getOpenLostReports() {
        return openLostReports;
    }

    public void setOpenLostReports(long openLostReports) {
        this.openLostReports = openLostReports;
    }

    public long getResolvedLostReports() {
        return resolvedLostReports;
    }

    public void setResolvedLostReports(long resolvedLostReports) {
        this.resolvedLostReports = resolvedLostReports;
    }
}
