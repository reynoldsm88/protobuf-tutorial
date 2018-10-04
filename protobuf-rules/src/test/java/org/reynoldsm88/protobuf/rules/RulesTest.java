package org.reynoldsm88.protobuf.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.reynoldsm88.protobuf.model.Input;
import org.reynoldsm88.protobuf.model.Output;

public class RulesTest {

    @Test
    public void rulesShouldReturnValidOutputObject() {
        Input input = Input.newBuilder().setName( "Michael" ).build();
        KieSession session = KieServices.Factory.get().newKieClasspathContainer().newKieSession( "protobuf-ksession" );

        session.insert( input );
        int firedCount = session.fireAllRules();

        Output output = (Output) session.getObjects( new ClassObjectFilter( Output.class ) ).stream().findFirst().get();

        assertEquals( 1, firedCount );
        assertEquals( 1, output.getCount() );
    }
}
