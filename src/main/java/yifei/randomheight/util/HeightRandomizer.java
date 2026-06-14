package yifei.randomheight.util;

import java.util.Random;

public class HeightRandomizer {
    private final Random random = new Random();
    private final double minScale;
    private final double maxScale;
    private final double minDifference;
    
    private float lastValue = -1;
    private float[] recentValues = new float[5];
    private int recentIndex = 0;
    private float forcedSmallNext = 0;
    
    public HeightRandomizer(double minScale, double maxScale) {
        this(minScale, maxScale, 0.5);
    }
    
    public HeightRandomizer(double minScale, double maxScale, double minDifference) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.minDifference = minDifference;
    }
    
    public float next() {
        float result;
        
        // 强制小值
        if (forcedSmallNext > 0) {
            result = generateRandomInRange(minScale, forcedSmallNext);
            forcedSmallNext = 0;
        } else {
            // 真正的随机选择
            result = generateFullyRandom();
        }
        
        // 确保与历史值有足够差异
        result = ensureDifferenceFromHistory(result);
        
        // 记录历史
        recentValues[recentIndex] = result;
        recentIndex = (recentIndex + 1) % recentValues.length;
        lastValue = result;
        
        return result;
    }
    
    private float generateFullyRandom() {
        // 真正的随机：在整个范围内随机
        double range = maxScale - minScale;
        double rand = random.nextDouble();
        
        // 使用指数分布让随机更分散
        double expRand = Math.pow(rand, 0.7); // 偏向两端
        return (float) (minScale + expRand * range);
    }
    
    private float generateRandomInRange(double min, double max) {
        double range = max - min;
        return (float) (min + random.nextDouble() * range);
    }
    
    private float ensureDifferenceFromHistory(float value) {
        // 检查与所有历史值的差值
        float minDiff = Float.MAX_VALUE;
        for (float recent : recentValues) {
            if (recent > 0) {
                float diff = Math.abs(value - recent);
                minDiff = Math.min(minDiff, diff);
            }
        }
        
        // 如果与某个历史值太接近，需要调整
        if (minDiff < minDifference && minDiff != Float.MAX_VALUE) {
            // 找到最接近的历史值
            float closest = Float.MAX_VALUE;
            for (float recent : recentValues) {
                if (recent > 0 && Math.abs(value - recent) == minDiff) {
                    closest = recent;
                    break;
                }
            }
            
            // 选择远离的方向
            if (value < closest) {
                value = (float) Math.min(closest - minDifference, maxScale);
            } else {
                value = (float) Math.max(closest + minDifference, minScale);
            }
            
            // 再次验证
            if (value > maxScale) {
                value = (float) maxScale;
            } else if (value < minScale) {
                value = (float) minScale;
            }
        }
        
        // 防止连续接近的极值
        if (lastValue > 0) {
            float diff = Math.abs(value - lastValue);
            if (diff < minDifference * 0.7) {
                // 翻转方向
                if (value < 1.0 && lastValue < 1.0) {
                    value = (float) (1.0 + (maxScale - 1.0) * random.nextDouble());
                    forcedSmallNext = 0;
                } else if (value >= 1.0 && lastValue >= 1.0) {
                    value = (float) (minScale + (1.0 - minScale) * random.nextDouble());
                    forcedSmallNext = 0;
                }
            }
        }
        
        // 防止连续3个值都偏向同一方向
        int smallCount = 0, largeCount = 0;
        for (float recent : recentValues) {
            if (recent > 0) {
                if (recent < 1.0) smallCount++;
                else largeCount++;
            }
        }
        
        if (smallCount >= 3) {
            forcedSmallNext = 0;
        } else if (largeCount >= 3) {
            forcedSmallNext = (float) (minScale + (1.0 - minScale) * 0.5);
        }
        
        return value;
    }
}