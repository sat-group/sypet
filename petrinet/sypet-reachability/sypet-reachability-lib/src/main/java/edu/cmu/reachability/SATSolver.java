package edu.cmu.reachability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.sat4j.core.VecInt;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

// import org.sat4j.minisat.SolverFactory;
// import org.sat4j.specs.ISolver;

public class SATSolver {

  private IPBSolver solver = null;
  private boolean unsat = false;
  private final VecInt assumptions;

  enum ConstraintType {
    LTE,
    EQ,
    GTE
  }

  // Maps the variable id to transition
  public final HashMap<Integer, Variable> id2variable = new HashMap<>();

  private int nbVariables = 0;
  public final VecInt loc_variables;

  public SATSolver() {
    solver = SolverFactory.newDefault();
    assumptions = new VecInt();
    loc_variables = new VecInt();
  }

  public void reset() {
    solver = SolverFactory.newDefault();
    unsat = false;
    id2variable.clear();
    nbVariables = 0;
  }

  public int getNbConstraints() {
    return solver.nConstraints();
  }

  public void setNbVariables(int vars) {

    // version for additional variables
    //		for (int i = vars+1; i <= vars+100; i++)
    //			loc_variables.push(i);
    //		nbVariables = vars+100;
    //		solver.newVar(nbVariables+100);
    //
    //		// dummy constraints for the additional variables
    //		// each variable much appear at least once in the solver
    //		for (int i = vars+1; i <= 100; i++) {
    //			try {
    //				solver.addAtLeast(new VecInt(new int[] {i}), 1);
    //			} catch (ContradictionException e) {
    //				assert(false);
    //			}
    //		}

    nbVariables = vars;
    solver.newVar(nbVariables);
  }

  public int getNbVariables() {
    return nbVariables;
  }

  public void addClause(VecInt constraint) {
    try {
      solver.addClause(constraint);
    } catch (ContradictionException e) {
      unsat = false;
    }
  }

  public void addConstraint(VecInt constraint, VecInt coeffs, ConstraintType ct, int k) {
    try {
      switch (ct) {
        case LTE:
          solver.addAtMost(constraint, coeffs, k);
          break;
        case EQ:
          solver.addExactly(constraint, coeffs, k);
          break;
        case GTE:
          solver.addAtLeast(constraint, coeffs, k);
          break;
        default:
          assert (false);
      }
    } catch (ContradictionException e) {
      unsat = true;
    }
  }

  public void addConstraint(VecInt constraint, ConstraintType ct, int k) {
    try {
      switch (ct) {
        case LTE:
          solver.addAtMost(constraint, k);
          break;
        case EQ:
          solver.addExactly(constraint, k);
          break;
        case GTE:
          solver.addAtLeast(constraint, k);
          break;
        default:
          assert (false);
      }
    } catch (ContradictionException e) {
      unsat = true;
    }
  }

  public void setAssumption(int v) {
    assumptions.push(v);
  }

  public void setTrue(int v) {
    try {
      VecInt clause = new VecInt(new int[] {v});
      solver.addClause(clause);
    } catch (ContradictionException e) {
      unsat = true;
    }
  }

  public void setFalse(int v) {
    try {
      VecInt clause = new VecInt(new int[] {-v});
      solver.addClause(clause);
    } catch (ContradictionException e) {
      unsat = true;
    }
  }

  public List<Variable> findPath(int loc) {

    ArrayList<Variable> res = new ArrayList<>();
    // TODO: what happens when loc -> loc+1
    // 1) initial state can be encoded as constraints
    // clear the assumptions: 1) final state, 2) blocking of models
    // set a new final state
    // set the previous state as true (you can use constraints -> setTrue)
    // incrementally increase the encoding to loc+1
    try {
      // comment the below assert when using assumptions
      assert (assumptions.isEmpty());
      if (!unsat && solver.isSatisfiable(assumptions)) {
        int[] model = solver.model();
        assert (model.length == nbVariables);
        VecInt block = new VecInt();
        for (Integer id : id2variable.keySet()) {
          if (model[id - 1] > 0) {
            block.push(-id);
            res.add(id2variable.get(id));
          }
        }

        // block model
        try {
          // ~getX(loc=1) OR ~setX(loc=2) OR ~setY(loc=3)
          // ~getX(loc=1) OR ~setX(loc=2) OR ~setY(loc=3) OR L1
          // block.push(loc_variables.get(loc-1));
          // assumptions.push(-loc_variables.get(loc-1));
          solver.addClause(block);
        } catch (ContradictionException e) {
          unsat = true;
        }
      }
    } catch (TimeoutException e) {
      // consider as it did not find a solution
      unsat = true;
    }

    // sort transitions by increasing time step
    Collections.sort(res);

    return res;
  }
}
