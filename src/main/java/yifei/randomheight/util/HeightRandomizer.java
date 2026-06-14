package yifei.randomheight.util;

import java.util.Random;

public class HeightRandomizer {
    private final Random random = new Random();
    private final double minScale;
    private final double maxScale;
    private final double minDifference;
    
    private float lastValue = -1;
    private int consecutiveLargeCount = 0;
    
    public HeightRandomizer(double minScale, double maxScale) {
        this(minScale, maxScale, 0.8);
    }
    
    public HeightRandomizer(double minScale, double maxScale, double minDifference) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.minDifference = minDifference;
    }
    
    public float next() {
        float result;
        
        // 规则2：连续两次1.x+后，下一次必须为0.x
        if (consecutiveLargeCount >= 2) {
            result = generateFromSmallRange();
            consecutiveLargeCount = 0;
        } else {
            // 规则1+3：随机选择区间
            if (random.nextBoolean()) {
                result = generateFromSmallRange();
                consecutiveLargeCount = 0;
            } else {
                result = generateFromLargeRange();
                consecutiveLargeCount++;
            }
            
            // 确保与上次的差值 >= minDifference
            result = ensureMinDifference(result);
        }
        
        lastValue = result;
        return result;
    }
    
    private float generateFromSmallRange() {
        return (float) (minScale + (1.0 - minScale) * random.nextDouble());
    }
    
    private float generateFromLargeRange() {
        return (float) (1.0 + (maxScale - 1.0) * random.nextDouble());
    }
    
    private float ensureMinDifference(float value) {
        if (lastValue == -1) {
            return value;
        }
        
        float diff = Math.abs(value - lastValue);
        if (diff < minDifference) {
            // 差值太小，需要调整
            if (value >= 1.0) {
                // 目标是大值，向上调整到满足差值
                value = (float) (lastValue + minDifference);
                if (value > maxScale) {
                    // 超出范围，向下调整
                    value = (float) Math.max(1.0, lastValue - minDifference);
                }
            } else {
                // 目标是0.x，向下调整到满足差值
                value = (float) (lastValue - minDifference);
                if (value < minScale) {
                    // 超出范围，向上调整
                    value = (float) Math.min(1.0, lastValue + minDifference);
                }
            }
        }
        
        return value;
    }
}