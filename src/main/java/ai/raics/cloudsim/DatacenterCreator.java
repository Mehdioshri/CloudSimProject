package ai.raics.cloudsim;

import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;

import java.util.ArrayList;
import java.util.List;

public class DatacenterCreator {

    /**
     * Creates a Datacenter and its Hosts.
     *
     * @param simulation the CloudSim simulation instance
     * @return the created Datacenter
     */
    public static Datacenter createDatacenter(CloudSimPlus simulation) {

        List<Host> hostList = new ArrayList<>();

        // Create Hosts
        for (int i = 0; i < SimulationConfig.NUMBER_OF_HOSTS; i++) {

            List<Pe> peList = new ArrayList<>();

            // Create CPUs (Processing Elements) for each Host
            for (int j = 0; j < SimulationConfig.HOST_PES; j++) {
                peList.add(
                    new PeSimple(SimulationConfig.HOST_MIPS)
                );
            }

            // Create Host
            Host host = new HostSimple(
                    SimulationConfig.HOST_RAM,
                    SimulationConfig.HOST_BW,
                    SimulationConfig.HOST_STORAGE,
                    peList
            );

            hostList.add(host);
        }

        // Create Datacenter
        Datacenter datacenter =
                new DatacenterSimple(simulation, hostList);

        return datacenter;
    }
}