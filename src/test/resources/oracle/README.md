# Ambrona unwinnability oracle

`ambrona-unwinnability.tsv` is generated from the final FENs cached in the
`PgnTestCase` fixtures. The generator calls Miguel Ambrona's D3-Chess C++
implementation through WSL and writes one row per distinct final FEN.

The TSV columns are:

```text
fen	fullWhite	fullBlack	quickWhite	quickBlack
```

## One-time Windows setup

1. Install WSL with Ubuntu from PowerShell or Windows Terminal:

```powershell
wsl --install -d Ubuntu
```

2. Reboot Windows if the installer asks for it.

3. Start Ubuntu from the Start menu, Windows Terminal, or PowerShell:

```powershell
wsl -d Ubuntu
```

4. In Ubuntu, install the build tools:

```bash
sudo apt update
sudo apt install -y build-essential git make
```

## Build D3-Chess in Ubuntu

Clone into the Ubuntu home directory, not into `/mnt/c/...`. Git operations on
the Windows-mounted filesystem can fail on permission/file-mode updates.

```bash
cd ~
git clone https://github.com/miguel-ambrona/D3-Chess.git
```

Build and install the Stockfish library used by D3-Chess:

```bash
cd ~/D3-Chess/lib/stockfish
make get-stockfish
make
sudo make install
sudo ldconfig
```

Build D3-Chess itself:

```bash
cd ~/D3-Chess/src
make
```

Optional smoke test:

```bash
LD_LIBRARY_PATH=/usr/local/lib ./cha
```

## Regenerate the oracle

Run this from the clean-chess checkout on Windows:

```powershell
mvn -q org.codehaus.mojo:exec-maven-plugin:3.6.2:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.dlb.chess.test.generate.GenerateAmbronaUnwinnabilityOracle"
```

By default the generator asks WSL for `$HOME/D3-Chess`. If D3-Chess lives
somewhere else inside Ubuntu, pass that path as the only Java argument:

```powershell
mvn -q org.codehaus.mojo:exec-maven-plugin:3.6.2:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.dlb.chess.test.generate.GenerateAmbronaUnwinnabilityOracle" "-Dexec.args=/home/<user>/D3-Chess"
```

Equivalent system-property form:

```powershell
mvn -q org.codehaus.mojo:exec-maven-plugin:3.6.2:java "-Dexec.classpathScope=test" "-Dexec.mainClass=com.dlb.chess.test.generate.GenerateAmbronaUnwinnabilityOracle" "-Dambrona.d3.path=/home/<user>/D3-Chess"
```

The generator compiles `tools/ambrona-oracle/cha_oracle.cpp` into `/tmp` inside
WSL, streams every distinct final FEN to it, and rewrites
`src/test/resources/oracle/ambrona-unwinnability.tsv` with LF line endings.
