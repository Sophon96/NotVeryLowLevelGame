package io.github.Sophon96.NotVeryLowLevelGame.engine;

public abstract class Scene {
    protected Director director;

    public Scene(Director director) {
        this.director = director;
    }

    public void update() {
    }

    public void render() {
    }

    public void onEnter() {
    }

    public void onExit() {
    }
}
