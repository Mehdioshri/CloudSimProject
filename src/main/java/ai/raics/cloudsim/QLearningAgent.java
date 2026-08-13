package ai.raics.cloudsim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QLearningAgent {

    /*
     * Q-Table:
     *
     * State -> (Action -> Q Value)
     */
    private final Map<State, Map<Action, Double>> qTable;

    private final double learningRate;
    private final double discountFactor;
    private final double epsilon;

    private final Random random;

    public QLearningAgent() {

        this.qTable = new HashMap<>();

        this.learningRate =
                SimulationConfig.LEARNING_RATE;

        this.discountFactor =
                SimulationConfig.DISCOUNT_FACTOR;

        this.epsilon =
                SimulationConfig.EPSILON;

        this.random =
                new Random(SimulationConfig.RANDOM_SEED);
    }

    /**
     * Selects an action using epsilon-greedy policy.
     *
     * With probability epsilon:
     *     explore a random VM.
     *
     * Otherwise:
     *     select the VM with the highest Q-value.
     */
    public Action chooseAction(
            State state,
            List<Action> availableActions) {

        initializeState(state, availableActions);

        // Exploration
        if (random.nextDouble() < epsilon) {

            int randomIndex =
                    random.nextInt(availableActions.size());

            return availableActions.get(randomIndex);
        }

        // Exploitation
        return getBestAction(state, availableActions);
    }

    /**
     * Updates the Q-value using the Q-Learning formula.
     *
     * Q(s,a) =
     * Q(s,a) +
     * α [r + γ max Q(s',a') - Q(s,a)]
     */
    public void updateQValue(
            State currentState,
            Action action,
            double reward,
            State nextState,
            List<Action> availableActions) {

        initializeState(currentState, availableActions);
        initializeState(nextState, availableActions);

        double currentQ =
                getQValue(currentState, action);

        double maxNextQ =
                getMaxQValue(nextState, availableActions);

        double newQ =
                currentQ
                + learningRate
                * (
                    reward
                    + discountFactor * maxNextQ
                    - currentQ
                );

        qTable
                .get(currentState)
                .put(action, newQ);
    }

    /**
     * Returns the Q-value for a State-Action pair.
     */
    public double getQValue(
            State state,
            Action action) {

        initializeState(
                state,
                List.of(action)
        );

        return qTable
                .get(state)
                .getOrDefault(action, 0.0);
    }

    /**
     * Returns the action with the highest Q-value.
     */
    private Action getBestAction(
            State state,
            List<Action> availableActions) {

        Action bestAction =
                availableActions.get(0);

        double bestQ =
                getQValue(state, bestAction);

        for (Action action : availableActions) {

            double qValue =
                    getQValue(state, action);

            if (qValue > bestQ) {

                bestQ = qValue;
                bestAction = action;
            }
        }

        return bestAction;
    }

    /**
     * Returns the maximum Q-value
     * among available actions.
     */
    private double getMaxQValue(
            State state,
            List<Action> availableActions) {

        double maxQ =
                Double.NEGATIVE_INFINITY;

        for (Action action : availableActions) {

            double qValue =
                    getQValue(state, action);

            if (qValue > maxQ) {
                maxQ = qValue;
            }
        }

        return maxQ;
    }

    /**
     * Initializes a State in the Q-Table.
     */
    private void initializeState(
            State state,
            List<Action> availableActions) {

        qTable.putIfAbsent(
                state,
                new HashMap<>()
        );

        Map<Action, Double> actions =
                qTable.get(state);

        for (Action action : availableActions) {

            actions.putIfAbsent(
                    action,
                    0.0
            );
        }
    }

    /**
     * Returns the number of states currently
     * stored in the Q-Table.
     */
    public int getNumberOfStates() {
        return qTable.size();
    }
}

