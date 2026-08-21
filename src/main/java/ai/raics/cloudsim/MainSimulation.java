package ai.raics.cloudsim;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;

import java.util.List;

public class MainSimulation {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("CloudSim Project Starting...");
        System.out.println("======================================");

        runBaseline();

        runFCFS();

        System.out.println();
        System.out.println("======================================");
        System.out.println("All simulations finished.");
        System.out.println("======================================");
    }


    private static void runBaseline() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("RUNNING BASELINE");
        System.out.println("======================================");

        CloudSimPlus simulation =
                new CloudSimPlus();

        Datacenter datacenter =
                DatacenterCreator.createDatacenter(simulation);

        List<Vm> vmList =
                VmCreator.createVms();

        DatacenterBroker broker =
                new DatacenterBrokerSimple(simulation);

        broker.submitVmList(vmList);

        PoissonTaskGenerator poissonGenerator =
                new PoissonTaskGenerator();

        double lambda =
                SimulationConfig.LAMBDA_1;

        List<Double> arrivalTimes =
                poissonGenerator.generateTasks(lambda);

        CloudletGenerator cloudletGenerator =
                new CloudletGenerator();

        List<Cloudlet> cloudletList =
                cloudletGenerator.createCloudlets(
                        arrivalTimes
                );
                MinMinScheduler minMinScheduler = new MinMinScheduler();
                minMinScheduler.schedule(cloudletList, vmList);

        broker.submitCloudletList(
                cloudletList
        );

        System.out.println();
        System.out.println(
                "Starting BASELINE simulation..."
        );

        simulation.start();

        List<Cloudlet> finishedCloudlets =
                broker.getCloudletFinishedList();

        ResultsAnalyzer analyzer =
                new ResultsAnalyzer();

        analyzer.analyze(
                finishedCloudlets,
                vmList,
                "BASELINE",
                lambda
        );

analyzer.analyze(
                finishedCloudlets,
                vmList,
                "Min-Min",
                lambda
        );





        System.out.println(
                "BASELINE completed Cloudlets: "
                        + finishedCloudlets.size()
        );
    }


    private static void runFCFS() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("RUNNING FCFS");
        System.out.println("======================================");

        CloudSimPlus simulation =
                new CloudSimPlus();

        Datacenter datacenter =
                DatacenterCreator.createDatacenter(simulation);

        List<Vm> vmList =
                VmCreator.createVms();

        DatacenterBroker broker =
                new DatacenterBrokerSimple(simulation);

        broker.submitVmList(vmList);

        PoissonTaskGenerator poissonGenerator =
                new PoissonTaskGenerator();

        double lambda =
                SimulationConfig.LAMBDA_1;

        List<Double> arrivalTimes =
                poissonGenerator.generateTasks(lambda);

        CloudletGenerator cloudletGenerator =
                new CloudletGenerator();

        List<Cloudlet> cloudletList =
                cloudletGenerator.createCloudlets(
                        arrivalTimes
                );


        // ======================================
        // FCFS SCHEDULER
        // ======================================

        FCFSScheduler fcfsScheduler =
                new FCFSScheduler();

        fcfsScheduler.schedule(
                cloudletList,
                vmList
        );


        // ======================================
        // Submit Cloudlets
        // ======================================

        broker.submitCloudletList(
                cloudletList
        );


        System.out.println();
        System.out.println(
                "Starting FCFS simulation..."
        );

        simulation.start();

        List<Cloudlet> finishedCloudlets =
                broker.getCloudletFinishedList();


        ResultsAnalyzer analyzer =
                new ResultsAnalyzer();

        analyzer.analyze(
                finishedCloudlets,
                vmList,
                "FCFS",
                lambda
        );


        System.out.println(
                "FCFS completed Cloudlets: "
                        + finishedCloudlets.size()
        );
    }
}
