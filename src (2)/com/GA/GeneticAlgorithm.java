package com.GA;

import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100; // 种群大小
    private static final int MAX_GENERATIONS = 100; // 最大迭代次数
    private static final double MUTATION_RATE = 0.1; // 变异率

    private static final int TARGET_VALUE = 100; // 目标值
    private static final int[] INITIAL_SOLUTION = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // 初始解

    public static void main(String[] args) {
        int[][] population = initializePopulation(POPULATION_SIZE, INITIAL_SOLUTION.length);

        int generation = 1;
        while (generation <= MAX_GENERATIONS) {
            int[] fitnessValues = calculateFitness(population);
            int bestFitness = getBestFitness(fitnessValues);
            int[] bestSolution = population[getBestSolutionIndex(fitnessValues)];

            System.out.println("Generation " + generation + ": Best Fitness = " + bestFitness);
            System.out.println("Best Solution: " + Arrays.toString(bestSolution));

            if (bestFitness == TARGET_VALUE) {
                System.out.println("Target value reached!");
                break;
            }

            int[][] nextGeneration = new int[POPULATION_SIZE][INITIAL_SOLUTION.length];
            for (int i = 0; i < POPULATION_SIZE; i++) {
                int[] parent1 = selectParent(population, fitnessValues);
                int[] parent2 = selectParent(population, fitnessValues);

                int[] child = crossover(parent1, parent2);
                child = mutate(child);

                nextGeneration[i] = child;
            }

            population = nextGeneration;
            generation++;
        }
    }

    // 初始化种群
    private static int[][] initializePopulation(int populationSize, int solutionLength) {
        int[][] population = new int[populationSize][solutionLength];
        Random random = new Random();

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < solutionLength; j++) {
                population[i][j] = random.nextInt(10) + 1; // 生成1到10的随机数作为初始解的元素
            }
        }

        return population;
    }

    // 计算适应度值
    private static int[] calculateFitness(int[][] population) {
        int[] fitnessValues = new int[population.length];

        for (int i = 0; i < population.length; i++) {
            int sum = Arrays.stream(population[i]).sum(); // 计算解的元素和作为适应度值
            fitnessValues[i] = sum;
        }

        return fitnessValues;
    }

    // 获取最佳适应度值
    private static int getBestFitness(int[] fitnessValues) {
        return Arrays.stream(fitnessValues).max().orElse(0);
    }

    // 获取最佳解的索引
    private static int getBestSolutionIndex(int[] fitnessValues) {
        return Arrays.stream(fitnessValues).boxed().collect(java.util.stream.Collectors.toList()).indexOf(getBestFitness(fitnessValues));
    }

    // 选择父代个体
    private static int[] selectParent(int[][] population, int[] fitnessValues) {
        Random random = new Random();
        int totalFitness = Arrays.stream(fitnessValues).sum();
        int cumulativeFitness = 0;
        double rouletteWheel = random.nextDouble() * totalFitness;

        for (int i = 0; i < population.length; i++) {
            cumulativeFitness += fitnessValues[i];
            if (cumulativeFitness >= rouletteWheel) {
                return population[i];
            }
        }

        return population[random.nextInt(population.length)];
    }

    // 交叉操作
    private static int[] crossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];
        Random random = new Random();
        int crossoverPoint = random.nextInt(parent1.length - 1) + 1;

        for (int i = 0; i < crossoverPoint; i++) {
            child[i] = parent1[i];
        }

        for (int i = crossoverPoint; i < parent2.length; i++) {
            child[i] = parent2[i];
        }

        return child;
    }

    // 变异操作
    private static int[] mutate(int[] solution) {
        Random random = new Random();

        for (int i = 0; i < solution.length; i++) {
            if (random.nextDouble() < MUTATION_RATE) {
                solution[i] = random.nextInt(10) + 1; // 随机选择一个新的值进行变异
            }
        }

        return solution;
    }
}
