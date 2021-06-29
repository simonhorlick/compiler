package syntax;

// Term describes things that can be evaluated.
interface Term {
  //
  boolean isNumericVal();

  //
  boolean isVal();

  // Evaluate this term by a single step.
  Term evaluateSingleStep();
}

