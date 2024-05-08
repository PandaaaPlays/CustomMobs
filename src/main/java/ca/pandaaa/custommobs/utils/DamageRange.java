package ca.pandaaa.custommobs.utils;

public class DamageRange {
    private double minimumDamage;
    private double maximimDamage;

    public DamageRange(double minimumDamage, double maximimDamage) {
        this.minimumDamage = minimumDamage;
        this.maximimDamage = maximimDamage;
    }

    public double getMinimumDamage() {
        return minimumDamage;
    }

    public double getMaximimDamage() {
        return maximimDamage;
    }
}
