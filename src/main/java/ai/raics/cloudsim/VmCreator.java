package ai.raics.cloudsim;

import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;

public class VmCreator {

    public static List<Vm> createVms() {

        List<Vm> vmList = new ArrayList<>();

        for (int i = 0; i < SimulationConfig.NUMBER_OF_VMS; i++) {

            Vm vm = new VmSimple(
                    SimulationConfig.VM_MIPS,
                    SimulationConfig.VM_PES
            );

            vm.setRam(SimulationConfig.VM_RAM);
            vm.setBw(SimulationConfig.VM_BW);
            vm.setSize(SimulationConfig.VM_SIZE);

            vmList.add(vm);
        }

        return vmList;
    }
}