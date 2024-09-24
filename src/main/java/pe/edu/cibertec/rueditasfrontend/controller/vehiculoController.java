package pe.edu.cibertec.rueditasfrontend.controller;
 import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditasfrontend.dto.VehiculoRequest;
import pe.edu.cibertec.rueditasfrontend.dto.VehiculoResponse;
import pe.edu.cibertec.rueditasfrontend.viewmodel.VehiculoModel;

@Controller
@RequestMapping("/vehiculo")
public class vehiculoController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String getBuscarVehiculo(Model model) {
        VehiculoModel vehiculoModel = new VehiculoModel("00","","",
                "","","","");
        model.addAttribute("vehiculoModel",vehiculoModel);
        return "vehiculo/buscar";

    }
    @PostMapping("/buscar")
    public String postBuscarVehiculo(@RequestParam("placa")String placa, Model model) {
        if(placa == null ||placa.trim().isEmpty()|| placa.length()!=7) {
            VehiculoModel vehiculoModel = new VehiculoModel("01","Ingresar la Placa correctamente para encontrar el auto","","",
                    "","","");
            model.addAttribute("vehiculoModel",vehiculoModel);
            return "vehiculo/buscar";

        }
        try{String endpoint="http://localhost:8081/api/vehiculos";
        VehiculoRequest vehiculoRequest = new VehiculoRequest(placa);
            VehiculoResponse vehiculoResponse = restTemplate.postForObject(endpoint, vehiculoRequest, VehiculoResponse.class);
            if(vehiculoResponse.codigo().equals("00")){
                VehiculoModel vehiculoModel = new VehiculoModel( "00", "",
                        vehiculoResponse.autoMarca(),
                        vehiculoResponse.autoModelo(),
                        vehiculoResponse.autoNroAsientos(),
                        vehiculoResponse.autoPrecio(),
                        vehiculoResponse.autoColor());
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "vehiculo/detalle";

            }else {
                VehiculoModel vehiculoModel= new VehiculoModel(
                        "01",
                        "No se encontró un vehículo para la placa ingresada",
                        "", "", "", "", "");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "vehiculo/buscar";
            }
        } catch (Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "99",
                    "Error: Ocurrió un problema inesperado",
                    "", "", "", "", "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            System.out.println(e.getMessage());
            return "vehiculo/buscar";
        }
    }
}
