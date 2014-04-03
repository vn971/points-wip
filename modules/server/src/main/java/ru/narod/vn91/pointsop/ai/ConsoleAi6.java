package ru.narod.vn91.pointsop.ai;

public interface ConsoleAi6 {

	void list_commands();

	void quit();

	void boardsize(int x, int y);

	void name();

	void version();

	void play(int x, int y, boolean color);

	void genmove(boolean color);

	void reg_genmove(boolean color);

	// complexity from 0 to 100
	void reg_genmove_with_complexity(boolean color, int complexity);

	void reg_genmove_with_time(boolean color, long milliseconds);

	void set_random_seed(int seed);

	void undo();

}
