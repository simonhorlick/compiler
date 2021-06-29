package syntax;

// Term describes things that can be evaluated.
interface Term {
  // Returns true if the term is a value (either zero, or successors of zero).
  boolean isNumericVal();

  // Returns true if the term is a value (numeric or boolean).
  boolean isVal();

  // Evaluate this term by a single step.
  Term evaluateSingleStep();
}

