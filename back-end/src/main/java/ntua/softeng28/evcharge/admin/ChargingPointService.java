package ntua.softeng28.evcharge.admin;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntua.softeng28.evcharge.operator.Operator;
import ntua.softeng28.evcharge.operator.OperatorRepository;
import ntua.softeng28.evcharge.charging_point.ChargingPoint;
import ntua.softeng28.evcharge.charging_point.ChargingPointRepository;
import ntua.softeng28.evcharge.charging_station.Address;
import ntua.softeng28.evcharge.charging_station.AddressRepository;
import ntua.softeng28.evcharge.charging_station.ChargingStation;
import ntua.softeng28.evcharge.charging_station.ChargingStationRepository;

@Service
public class ChargingPointService {
    @Autowired
    ChargingPointRepository chargingPointRepository;
    
    @Autowired
    ChargingStationRepository chargingStationRepository;
    
    @Autowired
    OperatorRepository operatorRepository;

    @Autowired
    AddressRepository addressRepository;

    public ChargingPointService() {
    }
    
    public void saveChargingPointsToDB(ChargingPointDataRequest chargingPointDataRequest){
        if(chargingPointDataRequest.getPointData() != null){
            for(ChargingPointData chargingPointData: chargingPointDataRequest.getPointData()){

                ChargingPoint newChargingPoint = new ChargingPoint();

                ChargingStation newChargingStation = null;
                Operator newOperator = null;
                Address newAddress = chargingPointData.getAddressInfo();
                List<ChargingStation> chargingStationsOfOperator = null;

                 //TODO: Perhaps create stations in a different way 
                 //Currently an operator is created with data given or autogenerated the a new station belonging to that operator is created
                if(chargingPointData.getOperator() != null){
                    newOperator = operatorRepository.findByName(chargingPointData.getOperator().getName()).orElse(null);

                    if(newOperator == null){
                        newOperator = new Operator();
                        // newOperator.setId(chargingPointData.getOperator().getId()); //currently autoset
                        newOperator.setName(chargingPointData.getOperator().getName());

                        newOperator = operatorRepository.save(newOperator);
                    }
                    else{
                        chargingStationsOfOperator = chargingStationRepository.findAllByOperator(newOperator);
                        newChargingStation = chargingStationsOfOperator.get(0);
                    }
                }
                else{
                    newOperator = new Operator();
                    newOperator = operatorRepository.save(newOperator);
                }

                if(newChargingStation == null){
                    if(chargingPointData.getAddressInfo() != null){
                        newAddress = addressRepository.save(newAddress);
                    }

                    newChargingStation = new ChargingStation();
                    newChargingStation.setChargingPoints(new HashSet<>());
                    newChargingStation.setOperator(newOperator);
                    newChargingStation.setAddress(newAddress);
                }

                newChargingPoint.setOperator(newOperator);

                newChargingPoint = chargingPointRepository.save(newChargingPoint);

                newChargingStation.getChargingPoints().add(newChargingPoint);
                chargingStationRepository.save(newChargingStation);
            }
        }
    }
}
