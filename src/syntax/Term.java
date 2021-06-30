package syntax;

// Term describes things that can be evaluated.
public interface Term {
  // Returns true if the term is a value (either zero, or successors of zero).
  boolean isNumericVal();

  // Returns true if the term is a value (numeric or boolean).
  boolean isVal();

  // Evaluate this term by a single step. When applied to a term that is not yet a value, it yields
  // the next step of evaluation for that term. When applied to a value an exception is thrown.
  Term evaluateSingleStep();
}
