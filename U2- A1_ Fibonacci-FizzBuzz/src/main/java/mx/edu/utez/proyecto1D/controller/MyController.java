package mx.edu.utez.proyecto1D.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/my-services")
public class MyController {


    @GetMapping("/fizzbuzz/{n}")
    public String servicioFizzBuzz(@PathVariable("n") int numero) {
        for (int i = 1; i <= numero; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
        return "Reyes Casimiro Stanle";
    }


    @GetMapping("/fibonacci/{n}")
    public String servicioFibonacci(@PathVariable("n") int numero) {
        int termino1 = 0;
        int termino2 = 1;

        for (int i = 1; i <= numero; i++) {
            if (i == 1) {
                System.out.println(termino1);
            } else if (i == 2) {
                System.out.println(termino2);
            } else {
                int siguiente = termino1 + termino2;
                System.out.println(siguiente);
                termino1 = termino2;
                termino2 = siguiente;
            }
        }
        return "Reyes Casimiro Stanle";
    }
}