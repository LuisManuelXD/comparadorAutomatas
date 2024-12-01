
package com.mycompany.proyectoautomatas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ProyectoAutomatas {

//    public static DFA inputDFA(String name) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("\nDefinamos el autómata " + name + ":");
//
//        // Entrada de estados
//        System.out.print("Estados (separados por espacios): ");
//        Set<String> states = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));
//
//        // Entrada de alfabeto
//        System.out.print("Alfabeto (separado por espacios): ");
//        Set<String> alphabet = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));
//
//        // Estado inicial
//        System.out.print("Estado inicial: ");
//        String startState = scanner.nextLine();
//
//        // Estados de aceptación
//        System.out.print("Estados de aceptación (separados por espacios): ");
//        Set<String> acceptStates = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));
//
//        // Transiciones
//        Map<String, Map<String, String>> transitions = new HashMap<>();
//        System.out.println("Introduce las transiciones en formato: estado actual, símbolo, estado siguiente");
//        System.out.println("Escribe 'fin' cuando hayas terminado.");
//
//        while (true) {
//            System.out.print("Transición: ");
//            String transition = scanner.nextLine();
//            if (transition.equals("fin")) {
//                break;
//            }
//
//            String[] parts = transition.split(" ");
//            String currentState = parts[0];
//            String symbol = parts[1];
//            String nextState = parts[2];
//
//            if (!states.contains(currentState) || !alphabet.contains(symbol) || !states.contains(nextState)) {
//                System.out.println("Transición inválida, asegúrate de que los estados y símbolos son correctos.");
//            } else {
//                transitions.computeIfAbsent(currentState, k -> new HashMap<>()).put(symbol, nextState);
//            }
//        }
//
//        return new DFA(states, alphabet, startState, acceptStates, transitions);
//    }
//
//    public static boolean areEquivalent(DFA dfa1, DFA dfa2) {
//        // Probamos algunas cadenas para ver si ambos autómatas aceptan las mismas
//        String[] testStrings = {"", "a", "b", "ab", "ba", "aa", "bb"};
//        for (String testString : testStrings) {
//            if (dfa1.accepts(testString) != dfa2.accepts(testString)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Verificación de equivalencia entre dos autómatas finitos deterministas (DFA)");
//
//        // Solicitar los dos DFA al usuario
//        DFA dfa1 = inputDFA("1");
//        DFA dfa2 = inputDFA("2");
//
//        // Verificar la equivalencia
//        if (areEquivalent(dfa1, dfa2)) {
//            System.out.println("\nLos autómatas son equivalentes.");
//        } else {
//            System.out.println("\nLos autómatas NO son equivalentes.");
//        }
//    }
}
