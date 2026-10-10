package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(680);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-53, -47, Math.toRadians(232)))
                .strafeTo(new Vector2d(-40,-34))
                .strafeToLinearHeading(new Vector2d(3, -10), Math.toRadians(270))
                .strafeTo(new Vector2d(3,-45))
                .strafeTo(new Vector2d(3,-40))
                .strafeToLinearHeading(new Vector2d(-40, -34), Math.toRadians(232))
                .strafeToLinearHeading(new Vector2d(30, -5), Math.toRadians(270))
                .strafeTo(new Vector2d(30,-50))
                .strafeTo(new Vector2d(30,-30))
                .strafeToLinearHeading(new Vector2d(-40,-34), Math.toRadians(232))
                .strafeTo(new Vector2d(-56,-34))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
