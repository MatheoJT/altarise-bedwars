package net.altarise.bw.impl;

import net.altarise.gameapi.scoreboard.GameScoreboard;

import java.util.Arrays;
import java.util.Collection;

public class SpecScoreboard extends GameScoreboard {
    @Override
    public Collection<String> getLines() {
        return Arrays.asList("", "§7Vous êtes en", "§7mode spectateur,", "§7pour rejouer,", "§7cliquez sur la boussole", "");
    }

    @Override
    public void onUpdate() {

    }
}
