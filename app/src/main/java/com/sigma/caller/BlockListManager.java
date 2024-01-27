package com.sigma.caller;

import java.util.ArrayList;
import java.util.List;

public class BlockListManager {

    private static List<String> blockList = new ArrayList<>();

    public BlockListManager() {
        blockList.add("998996099335");
    }

    public static void addBlockedNumber(String number) {
        blockList.add(number);
    }

    public static void removeBlockedNumber(String number) {
        blockList.remove(number);
    }

    public static boolean isNumberBlocked(String number) {
        return blockList.contains(number);
    }

    public static List<String> getBlockedNumbers() {
        return blockList;
    }
}
