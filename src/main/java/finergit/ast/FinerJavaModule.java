package finergit.ast;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import finergit.ast.token.JavaToken;

public abstract class FinerJavaModule {

  public final String name;
  public final FinerJavaModule outerModule;
  private final List<JavaToken> tokens;

  FinerJavaModule(final String name, final FinerJavaModule outerModule) {
    this.name = name;
    this.outerModule = outerModule;
    this.tokens = new ArrayList<>();
  }

  public boolean addToken(final JavaToken token) {
    return this.tokens.add(token);
  }

  public void clearTokens() {
    this.tokens.clear();
  }

  public List<JavaToken> getTokens() {
    return this.tokens;
  }

  public List<String> getLines() {
    return this.tokens.stream()
        .map(t -> t.value)
        .collect(Collectors.toList());
  }

  public abstract Path getDirectory();

  public abstract String getFileName();

  /**
   * ベースネーム（拡張子がないファイル名を返す．
   * 
   * @return
   */
  public final String getBaseName() {
    final StringBuilder builder = new StringBuilder();
    if ((null != this.outerModule) && !(this.outerModule instanceof FinerJavaFile)) {
      builder.append(this.outerModule.getBaseName());
      builder.append("$");
    }
    builder.append(this.name);
    return builder.toString();
  }

  public final Path getPath() {
    return this.getDirectory()
        .resolve(this.getFileName());
  }

  public abstract String getExtension();
}
