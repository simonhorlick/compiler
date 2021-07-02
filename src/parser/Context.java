package parser;

import java.util.ArrayList;
import java.util.List;

public class Context {

  private final List<String> names;
  private final int free;

  public Context(List<String> names, int free) {
    this.names = names;
    this.free = free;
  }

  public List<String> names() {
    return names;
  }

  public Context addBinder(String absName) {
    List<String> l = new ArrayList<>(names);
    l.add(absName);
    return new Context(l, free);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("Context({");
    for (int i = 0; i < names.size(); i++) {
      if (i != 0) {
        s.append(", ");
      }
      s.append(names.size() - 1 - i).append(":").append(names.get(i));
    }
    return s.append("})").toString();
  }

  public int getDeBruijinIndex(String name) {
    return names.size() - 1 - names.indexOf(name);
  }
}
