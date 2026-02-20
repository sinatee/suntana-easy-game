package com.ponggame.game.entity;

public class AIController {

    Paddle paddle;
    Ball ball;

    AIController(Paddle p, Ball b) {
        paddle = p;
        ball = b;
    }

    void update(double dt) {

        if (ball.y < paddle.y)
            paddle.y -= paddle.speed * dt;
        else if (ball.y > paddle.y + paddle.height)
            paddle.y += paddle.speed * dt;
    }
    
}
