/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import sk.shanki.lp.compilation.CompiledRule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.Atom;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.ObjectConstant;
import sk.shanki.lp.Rule;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GRule extends CompiledRule<GRule,GLiteral,GLiterals> {

    public GRule(Rule original, GLiterals head, GLiterals positive, GLiterals negative) {
        super(original, head, positive, negative);
    }
    
   // toto sa da optimalizovat, aby sa to vytvorilo iba raz
    // ako spravit, aby tieto literaly boli zarucene ine od tych v povodnom programe?
    
    Atom toGamma() {
        return new Atom("gamma", new ObjectConstant(original));
    }
    
    Atom toNegGamma() {
        return new Atom("neggamma", new ObjectConstant(original));
    }    

    Atom toAppPrime() {
        return new Atom("appprime", new ObjectConstant(original));
    }
    
    Atom toNegAppPrime() {
        return new Atom("negappprime", new ObjectConstant(original));
    }    
    
    Atom toApp() {
        return new Atom("app", new ObjectConstant(original));
    }
    
    Atom toNegApp() {
        return new Atom("negapp", new ObjectConstant(original));
    }

    
    Atom toDef() {
        return new Atom("def", new ObjectConstant(original));
    }
    
    Atom toNegDef() {
        return new Atom("negdef", new ObjectConstant(original));
    }

    Atom toDefPrime() {
        return new Atom("defprime", new ObjectConstant(original));
    }
    
    Atom toOk() {
        return new Atom("ok", new ObjectConstant(original));
    }
    
    Atom toLessDef() {
        return new Atom("lessdef", new ObjectConstant(original));
    }
    
    public Rule toRule0(boolean isGenerating) {
        Literal nhead;
        
        if (isGenerating) {
            nhead = this.toGamma();
        } else {
            nhead = this.toNegGamma();
        }
        
        return new Rule(nhead);
    }
    
    public Rule toRule1() {
        Literal nhead   = head.getSingleton().toLit();
        NafLiterals body = new NafLiterals(this.toApp());
        
        return new Rule(nhead, body);
    }
    
    public Rule toRulePrime1() {
        Literal nhead    = head.getSingleton().toLitPrime();
        NafLiterals body  = new NafLiterals(this.toAppPrime());
        
        return new Rule(nhead, body);
    }     
    
    public Rule toRule2() {
        NafLiterals nhead   = new NafLiterals(this.toApp(), this.toNegApp());
        NafLiterals body     = positive.toLit();
        
        return new Rule(nhead, body);
    }
    
    public Rule toRulePrime2() {
        NafLiterals nhead    = new NafLiterals(this.toAppPrime(), this.toNegAppPrime());
        NafLiterals body      = positive.toLitPrime();
       
       return new Rule(nhead, body);
    }    
    
    public Set<Rule> toRule3() {
        Set<Rule> rules = new HashSet<>();
        
        for (GLiteral lit : positive) {
            Literal nhead   = this.toNegApp();
            NafLiterals body = new NafLiterals(lit.toNot());
            
            rules.add(new Rule(nhead, body));
        }
        
        return rules;
    }
    
    public Set<Rule> toRulePrime3() {
        Set<Rule> rules = new HashSet<>();
        
        for (GLiteral literal : positive) {
            Literal nhead   = this.toNegAppPrime();
            NafLiterals body = new NafLiterals(literal.toNotPrime());
            
            rules.add(new Rule(nhead, body));
        }
        
        return rules;
    }
    
    public Rule toRulePrime5() {
        Literal nhead   = this.toNegAppPrime();
        NafLiterals body = new NafLiterals(this.toNegGamma());
        
        return new Rule(nhead, body);
    }    
    
    public Rule toRulex() {
        Literal nhead   = new Atom("hasnongen");
        NafLiterals body = new NafLiterals(this.toApp(), this.toNegGamma());
        
        return new Rule(nhead, body);
    }    

    public Set<Rule> toRuleDef1() {
        
        Set<Rule> rules = new HashSet<>();
        
        for (GLiteral literal : negative) {
            for (GRule defeater : literal.inHead()) {
                Literal nHead   = this.toDef();
                NafLiterals body = new NafLiterals(this.toApp(), defeater.toAppPrime());

                rules.add(new Rule(nHead, body));
            }
        }
        
        return rules;
    }    

    public Set<Rule> toRuleDefPrime1() {
        
        Set<Rule> rules = new HashSet<>();
        
        for (GLiteral literal : negative) {
            for (GRule defeater : literal.inHead()) {
                Literal nHead   = this.toDefPrime();
                NafLiterals body = new NafLiterals(this.toAppPrime(), defeater.toApp());

                rules.add(new Rule(nHead, body));
            }
        }

        return rules;
    }
    
    public Rule toRuleDef2() {
        Literal nHead                   = this.toNegDef();
        ArrayList<NafLiteral> bodyLits   = new ArrayList<>();

        bodyLits.add(this.toApp());
        for (GLiteral literal : negative) {
            for (GRule defeater : literal.inHead()) {
                bodyLits.add(defeater.toNegAppPrime());
            }
        }
        
        NafLiterals body = new NafLiterals(bodyLits);
        
        return new Rule(nHead, body);
    }
    
    public Rule toRuleOk1() {
        Literal nHead       = this.toOk();
        NafLiterals body     = new NafLiterals(this.toNegDef());
        
        return new Rule(nHead, body);
    }    
    
    public Rule toRuleOk2() {
        Literal nHead       = this.toOk();
        NafLiterals body     = new NafLiterals(this.toDef(), this.toLessDef());
        
        return new Rule(nHead, body);
    }
    
    public Rule toRuleOk3() {
        Literal nHead       = this.toOk();
        NafLiterals body     = new NafLiterals(this.toNegApp());
        
        return new Rule(nHead, body);
    }
    
    public Set<Rule> toRuleOk4() {
        Set<Rule> rules = new HashSet<>();
        
        for (GRule less : lessRules) {
            Literal nHead   = this.toLessDef();
            NafLiterals body = new NafLiterals(this.toApp(), less.toDefPrime());
            
            rules.add(new Rule(nHead, body));
        }
        
        return rules;
    }
    
    public Set<Rule> toRuleSaturation() {
        Set<Rule> rules = new HashSet<>();
        
        rules.add(new Rule(this.toAppPrime(), new NafLiterals(new Atom("allok"))));
        rules.add(new Rule(this.toNegAppPrime(), new NafLiterals(new Atom("allok"))));        

        rules.add(new Rule(this.toDef(), new NafLiterals(new Atom("allok"))));
        rules.add(new Rule(this.toNegDef(), new NafLiterals(new Atom("allok"))));
        rules.add(new Rule(this.toDefPrime(), new NafLiterals(new Atom("allok"))));

        rules.add(new Rule(this.toOk(), new NafLiterals(new Atom("allok"))));        
        rules.add(new Rule(this.toLessDef(), new NafLiterals(new Atom("allok")))); 

        return rules;
    }    

}