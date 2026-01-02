package io.github.Sophon96.NotVeryLowLevelGame;

import io.github.Sophon96.NotVeryLowLevelGame.engine.*;
import io.github.Sophon96.NotVeryLowLevelGame.scenes.NineMonkeysScene;

public class Main {
    public static void main(String[] args) {
        Director director = new Director();

        Scene nms = new NineMonkeysScene(director);

        director.changeScene(nms);
        director.run();
    }
}