package com.example.jamesoneill.three_in_a_row;

class Score {
    private String name;
    private byte gridSize;
    private String difficulty;
    private String time;

    public Score(String name, byte gridSize, String difficulty, String time) {
        this.name = name;
        this.gridSize = gridSize;
        this.difficulty = difficulty;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getGridSize() {
        return gridSize;
    }

    public void setGridSize(byte gridSize) {
        this.gridSize = gridSize;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Score{" +
                "name='" + name + '\'' +
                ", gridSize=" + gridSize +
                ", difficulty='" + difficulty + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
