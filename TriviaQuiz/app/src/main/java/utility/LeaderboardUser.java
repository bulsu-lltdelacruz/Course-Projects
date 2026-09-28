package utility;

public class LeaderboardUser {
    public String uid;
    public String username;
    public String rank;
    public int rankPoints;
    public int totalGames;
    public int totalCorrectItems;
    public int coins;

    public LeaderboardUser() {

    }

    public double getWinRate() {
        if (totalGames == 0) return 0.0;
        return (double) totalCorrectItems / totalGames;
    }
    public int getRankTierOrder() {
        switch (rank) {
            case StringValues.RANK_ORACLE:     return 7;
            case StringValues.RANK_GENIUS:     return 6;
            case StringValues.RANK_MASTER:     return 5;
            case StringValues.RANK_SAGE:       return 4;
            case StringValues.RANK_SCHOLAR:    return 3;
            case StringValues.RANK_APPRENTICE: return 2;
            case StringValues.RANK_NOVICE:
            default:                           return 1;
        }
    }
}
