/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import sk.shanki.lp.predicates.GroundingPredicateFactory;
import sk.shanki.lp.grounder.GroundingRule;
import sk.shanki.lp.compilation.CompilationFactory;
import sk.shanki.lp.compilation.CompiledLiteral;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.compilation.CompiledLiterals;
import sk.shanki.lp.compilation.CompiledProgram;
import sk.shanki.lp.compilation.CompiledRule;
import sk.shanki.lp.smodels.SmodelsLiteral;
import sk.shanki.lp.smodels.SmodelsLiteralCache;
import sk.shanki.lp.smodels.SmodelsRule;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 * 
 * Immutable
 */
public class Rule {
    private final String name;
    private final NafLiterals head;
    private final NafLiterals body;
    private final boolean partial;
    
    public Rule(boolean partial, String name, NafLiterals head, NafLiterals body) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (head.isEmpty()) {
            throw new IllegalArgumentException("Head cannot be empty.");
        }
        
        this.partial = partial;
        this.name   = name;
        this.head   = head;
        this.body   = body;
    }

    public Rule(boolean partial, String name, NafLiteral head, NafLiterals body) {
        this.partial = partial;
        this.name   = name;
        
        ArrayList<NafLiteral> literals = new ArrayList<>();
        literals.add(head);
        
        this.head   = new NafLiterals(literals);
        this.body   = body;
    }

    public Rule(String name, NafLiteral head) {
        this(false, name, head, new NafLiterals());
    }
    
    public Rule(Literal head) {
        this("", head);
    }    

    public Rule(String name, NafLiterals body) {
        this(false, name, new NafLiterals(), body);
    }
    
    public Rule(Literal head, NafLiterals body){
        this(false, "", head, body);
    }
    
    public Rule(NafLiterals head, NafLiterals body) {
        this(false, "", head, body);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.head);
        hash = 97 * hash + Objects.hashCode(this.body);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rule other = (Rule) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.head, other.head)) {
            return false;
        }
        return Objects.equals(this.body, other.body);
    }

    public NafLiterals getHead() {
        return head;
    }
    
    Set<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();
        
        atoms.addAll(head.collectAllAtoms());
        atoms.addAll(body.collectAllAtoms());
        
        return atoms;
    }
    
    Set<Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        literals.addAll(head.collectAllLiterals());
        literals.addAll(body.collectAllLiterals());
        
        return literals;
    }    

    public SmodelsRule toSmodelsRule(SmodelsLiteralCache cache) {
        
        SmodelsLiteral shead        = cache.ensure(head.getSingleton().getLiteral());
        List<SmodelsLiteral> spos   = body.getPositiveLiterals().toSmodelsLiteral(cache);
        List<SmodelsLiteral> sneg   = body.getNegativeLiterals().toSmodelsLiteral(cache);
        
        return new SmodelsRule(shead, spos, sneg);
    } 

    Rules toMetaProgram() {
        Rules meta = new Rules();
        
        meta.add(new Rule(new Atom("rule", new ObjectConstant(this))));
        
        for (NafLiteral nafLiteral : head) {
            meta.add(new Rule(new Atom("head", new ObjectConstant(nafLiteral), new ObjectConstant(this))));
        }
        
        for (NafLiteral nafLiteral : body) {
            if (nafLiteral instanceof Not) {
                Literal literal = ((Not)nafLiteral).getLiteral();
                meta.add(new Rule(new Atom("nbl", new ObjectConstant(literal), new ObjectConstant(this))));
            } else if (nafLiteral instanceof Atom) {
                meta.add(new Rule(new Atom("pbl", new ObjectConstant(nafLiteral), new ObjectConstant(this))));
            }
        }
        
        return meta;
    }

    public boolean hasName() {
        return name.isEmpty() == false;
    }

    public String getName() {
        return name;
    }

    public boolean hasBodySatisfiedIn(AnswerSet answerSet) {
        return body.isSatisfiedIn(answerSet);
    }

    public NafLiteral getHeadSingleton() {
        return head.getSingleton();
    }
    
    public Literal getHeadLiteral() {
        return (Literal)head.getSingleton();
    }

//    public Predicates getPositiveBody() {
//        return body.getPositiveBody();
//    } 

    boolean isGenerating(AnswerSet answerSet) {
        return hasBodySatisfiedIn(answerSet);
    }

    <P extends CompiledProgram,R extends CompiledRule,L extends CompiledLiteral,LS extends CompiledLiterals> CompiledRule compile(CompilationFactory<P,R,L,LS> factory, CompiledLiteralCache<L> cache) {
        // TODO: co budem robit s nefaultovymi negaciami v hlave?
        LS tHead         = head.getPositiveLiterals().compile(factory, cache);//cache.ensure(head.getSingleton().getLiteral());
        LS tPosBody      = body.getPositiveLiterals().compile(factory, cache);
        LS tNegBody      = body.getNegativeLiterals().compile(factory, cache);
        
        return factory.createRule(this, tHead, tPosBody, tNegBody);
    }

    GroundingRule toGroundingRule(GroundingPredicateFactory factory) {
        Literals posBody = body.getPositiveLiterals();
        Literals negBody = body.getNegativeLiterals();
        
        Literals.Carret split = posBody.split(factory);
        
        return new GroundingRule(this, head, split.literals, negBody, split.predicates);

    }

    public boolean hasDisjunction() {
        return head.isSingleton() == false;
    }

    public NafLiterals body() {
        return body;
    }

    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printRule(name, head, body);
    }

    public Rule rewriteTermsToIds(ObjectConstantMapping mapping) {
        NafLiterals nHead = head.rewriteElementsToIds(mapping);
        NafLiterals nBody = body.rewriteElementsToIds(mapping);
        
        return new Rule(
                partial,
                name,
                nHead,
                nBody);
    }

    public boolean isGround() {
        return head.isGround() && body.isGround();
    }

    public boolean defines(String predicateName) {
        return head.isSingleton() && head.getSingleton() instanceof Literal && head.getSingleton().getLiteral().isOfSymbol(predicateName);
    }

    boolean isPartial() {
        return partial;
    }
}