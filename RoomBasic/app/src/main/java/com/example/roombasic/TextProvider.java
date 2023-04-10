package com.example.roombasic;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TextProvider {

    private List<String> enStrings = new ArrayList<>(
            Arrays.asList("Apple", "Google", "Microsoft", "Adobe", "Facebook", "Oracle"));

    private List<String> chStrings = new ArrayList<>(
            Arrays.asList("蘋果", "谷歌", "微軟", "土坯", "臉書", "甲骨文"));

    private Random random = new Random();

    public Pair<String, String> GetRandText() {
        int index = random.nextInt(enStrings.size());
        return new Pair<>(enStrings.get(index), chStrings.get(index));
    }
}
