
package com.mycompany.proyectoautomatas;

//import java.util.*;
//
//class DFA {
//    Set<String> states;
//    Set<String> alphabet;
//    String startState;
//    Set<String> acceptStates;
//    Map<String, Map<String, String>> transitions;
//
//    public DFA(Set<String> states, Set<String> alphabet, String startState, Set<String> acceptStates, Map<String, Map<String, String>> transitions) {
//        this.states = states;
//        this.alphabet = alphabet;
//        this.startState = startState;
//        this.acceptStates = acceptStates;
//        this.transitions = transitions;
//    }
//
//    public boolean accepts(String input) {
//        String currentState = startState;
//        for (char symbol : input.toCharArray()) {
//            String symbolStr = String.valueOf(symbol);
//            if (!alphabet.contains(symbolStr) || !transitions.containsKey(currentState) || !transitions.get(currentState).containsKey(symbolStr)) {
//                return false;
//            }
//            currentState = transitions.get(currentState).get(symbolStr);
//        }
//        return acceptStates.contains(currentState);
//    }
//}
import java.util.*;

public class DFA {
    Set<String> states;
    Set<String> alphabet;
    Map<String, Map<String, String>> transitions;
    String startState;
    Set<String> acceptingStates;

    public DFA(Set<String> states, Set<String> alphabet, Map<String, Map<String, String>> transitions, String startState, Set<String> acceptingStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.acceptingStates = acceptingStates;
    }

    public boolean isEquivalent(DFA other) {
        Set<String> visited = new HashSet<>();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(this.startState, other.startState));

        while (!queue.isEmpty()) {
            Pair current = queue.poll();
            if (visited.contains(current.toString())) continue;

            visited.add(current.toString());
            boolean thisAccepts = this.acceptingStates.contains(current.state1);
            boolean otherAccepts = other.acceptingStates.contains(current.state2);

            if (thisAccepts != otherAccepts) return false;

            for (String symbol : this.alphabet) {
                String nextState1 = this.transitions.getOrDefault(current.state1, new HashMap<>()).get(symbol);
                String nextState2 = other.transitions.getOrDefault(current.state2, new HashMap<>()).get(symbol);

                if (nextState1 != null && nextState2 != null) {
                    queue.add(new Pair(nextState1, nextState2));
                }
            }
        }
        return true;
    }

    static class Pair {
        String state1, state2;

        Pair(String state1, String state2) {
            this.state1 = state1;
            this.state2 = state2;
        }

        @Override
        public String toString() {
            return state1 + "," + state2;
        }
    }
}