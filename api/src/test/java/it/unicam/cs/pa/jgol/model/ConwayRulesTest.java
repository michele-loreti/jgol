package it.unicam.cs.pa.jgol.model;

import it.unicam.cs.pa.jgol.Controller;
import it.unicam.cs.pa.jgol.model.conway.ConwayField;
import it.unicam.cs.pa.jgol.model.conway.ConwayState;
import it.unicam.cs.pa.jgol.model.conway.SurviveRule;
import it.unicam.cs.pa.jgol.model.conway.UnderpopulationRule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ConwayRulesTest {

    @Test
    public void aLiveCellShouldSurvive() {
        ConwayState s = ConwayState.LIVE;
        List<ConwayState> others = getOthers(2,2);
        SurviveRule rule = new SurviveRule();
        testRuleResult(s, others, rule, ConwayState.LIVE);
    }

    @Test
    public void shouldDeadForUnderpopulation() {
        ConwayState s = ConwayState.LIVE;
        List<ConwayState> others = getOthers(3,1);
        UnderpopulationRule rule = new UnderpopulationRule();
        testRuleResult(s, others, rule, ConwayState.DEAD);
    }

    @Test
    public void ruleForUnderpopulationShouldNotBeApplicable() {
        ConwayState s = ConwayState.LIVE;
        List<ConwayState> others = getOthers(2,2);
        UnderpopulationRule rule = new UnderpopulationRule();
        testRuleIsNotEnabled(s, others, rule);
    }

    @Test
    public void ruleForUnderpopulationOnDeadState() {
        ConwayState s = ConwayState.DEAD;
        List<ConwayState> others = getOthers(3,2);
        UnderpopulationRule rule = new UnderpopulationRule();
        testRuleIsNotEnabled(s, others, rule);
    }

    private void testRuleResult(ConwayState s, List<ConwayState> others, Rule<ConwayState> rule, ConwayState expected) {
        Optional<ConwayState> result = rule.apply(s, others);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    private void testRuleIsNotEnabled(ConwayState s, List<ConwayState> others, UnderpopulationRule rule) {
        Optional<ConwayState> result = rule.apply(s, others);
        assertFalse(result.isPresent());
    }


    private List<ConwayState> getOthers(int dead, int live) {
        return IntStream.range(0, dead+live)
                .mapToObj(i -> (i<dead?ConwayState.DEAD:ConwayState.LIVE))
                .toList();
    }

    @Test
    public void testOscillator() {
        Rule<ConwayState> rules = ConwayState.DEFAULT_RULES;
        ConwayField<GridCoordinates> field = new ConwayField<>();
        field.setAlive(new GridCoordinates(8,6));
        field.setAlive(new GridCoordinates(8,7));
        field.setAlive(new GridCoordinates(8,8));
        Environment<ConwayState, GridCoordinates> field2 = field.apply(rules);
        assertEquals(ConwayState.DEAD, field2.statusOf(new GridCoordinates(8,6)));
        assertEquals(ConwayState.LIVE, field2.statusOf(new GridCoordinates(7,7)));
        assertEquals(ConwayState.LIVE, field2.statusOf(new GridCoordinates(8,7)));
        assertEquals(ConwayState.LIVE, field2.statusOf(new GridCoordinates(9,7)));
        assertEquals(ConwayState.DEAD, field2.statusOf(new GridCoordinates(8,8)));
    }

    @Test
    public void testOscillatorController() {
        Controller<ConwayState, GridCoordinates> controller = Controller.getConwayController();
        controller.set(new GridCoordinates(0,0), ConwayState.LIVE);
        controller.set(new GridCoordinates(0,1), ConwayState.LIVE);
        controller.set(new GridCoordinates(0,2), ConwayState.LIVE);
        controller.stepForward();
        assertEquals(ConwayState.DEAD, controller.getStateOf(new GridCoordinates(0,0)));
        assertEquals(ConwayState.LIVE, controller.getStateOf(new GridCoordinates(-1,1)));
        assertEquals(ConwayState.LIVE, controller.getStateOf(new GridCoordinates(0,1)));
        assertEquals(ConwayState.LIVE, controller.getStateOf(new GridCoordinates(1,1)));
        assertEquals(ConwayState.DEAD, controller.getStateOf(new GridCoordinates(0,2)));
    }

}