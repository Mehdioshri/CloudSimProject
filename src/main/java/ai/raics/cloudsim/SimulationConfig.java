

package ai.raics.cloudsim;

public class SimulationConfig {

    // =========================
    // Simulation Parameters
    // =========================

    // Number of Cloudlets (Tasks)
    public static final int NUMBER_OF_CLOUDLETS = 1000;

    // Number of Virtual Machines
    public static final int NUMBER_OF_VMS = 20;


    // =========================
    // VM Parameters
    // =========================

    // VM MIPS
    public static final long VM_MIPS = 1000;

    // Number of CPU cores for each VM
    public static final int VM_PES = 2;

    // VM RAM in MB
    public static final long VM_RAM = 4096;

    // VM Bandwidth
    public static final long VM_BW = 10000;

    // VM Storage in MB
    public static final long VM_SIZE = 10000;


    // =========================
    // Host Parameters
    // =========================

    // Number of Hosts
    public static final int NUMBER_OF_HOSTS = 10;

    // Host MIPS
    public static final long HOST_MIPS = 10000;

    // Number of CPU cores per Host
    public static final int HOST_PES = 8;

    // Host RAM in MB
    public static final long HOST_RAM = 32768;

    // Host Bandwidth
    public static final long HOST_BW = 100000;

    // Host Storage in MB
    public static final long HOST_STORAGE = 1000000;


    // =========================
    // Cloudlet Parameters
    // =========================

    // Minimum task length
    public static final long MIN_TASK_LENGTH = 1000;

    // Maximum task length
    public static final long MAX_TASK_LENGTH = 10000;

    // Number of CPU cores required by each task
    public static final int CLOUDLET_PES = 1;


    // =========================
    // Poisson Arrival Rates
    // =========================

    public static final double LAMBDA_1 = 20.0;

    public static final double LAMBDA_2 = 10.0;

    public static final double LAMBDA_3 = 5.0;


    // =========================
    // Q-Learning Parameters
    // =========================

    // Learning rate
    public static final double LEARNING_RATE = 0.1;

    // Discount factor
    public static final double DISCOUNT_FACTOR = 0.9;

    // Exploration probability
    public static final double EPSILON = 0.1;

    // Number of training episodes
    public static final int NUMBER_OF_EPISODES = 100;


    // =========================
    // Random Seed
    // =========================

    public static final long RANDOM_SEED = 42;


    // =========================
    // Constructor
    // =========================

    private SimulationConfig() {
        // Prevent object creation
    }
}
