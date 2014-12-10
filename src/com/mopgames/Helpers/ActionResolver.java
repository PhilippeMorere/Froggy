package com.mopgames.Helpers;

public interface ActionResolver {
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(int score);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();
	
	public void rateGame();
	
	public void showAd(boolean show);

	public void share(String text, String appLink);
}