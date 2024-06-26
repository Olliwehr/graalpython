# Scripts to build wheels for GraalPy.

[GraalPy](https://github.com/oracle/graalpython) is compatible with many Python libraries, including those that extend the Python runtime with native code.
However, implemented in Java and thus binary incompatible with existing extensions, users of native Python extension libraries such as NumPy, SciPy, or PyTorch have to build their own binaries when installing these libraries.
For many libraries, this means installing additional build dependencies and sitting through long and resource-intensive compilation processes.

This project is meant to be a place for the community to collect build recipes for as many popular packages as possible that can then be built once with GitHub Actions for each major release of GraalPy.

## Quickstart

1. [Fork](../../../../fork) this repository.
2. Go to the [actions](../../../../actions) on your fork.
3. On the left, choose the Workflow for the OS you are interested in.

   ![](guide01.png)

4. Click on "Run workflow".
   You can enter a package name or build all packages.
   See [the spec list](../../../../blob/master/scripts/wheelbuilder/generate_workflow.py) for which packages are available.

   ![](guide02.png)

## How to contribute

There should be only one relevant file, `generate_workflow.py`.
In it we collect `BuildSpec` objects that define how to build a particular package.
Many packages do not need special definitions, just a name and maybe which platforms to build it for.
System package dependencies can be specified by platform where needed.
Dependencies between specs are not strictly necessary, but can reduce the overall build times and thus resource usage of GitHub Action runners.

Changes to `generate_workflow.py` are reflected in the build specs by running the file.
It creates GitHub Action workflow files, one for each platform, and a giant one with all jobs.

## How to run this

Many packages use a lot of resources to build, and even those that do not quickly add up.
We have chosen GitHub Action workflows as the cross-platform specification for how to build packages.

### Running actions locally with nektos/act

[Act](https://github.com/nektos/act) allows running GitHub actions locally.
We can use that to just build packages on a local machine:

```
./act --artifact-server-path /tmp/artifacts \
      -W .github/workflows/build-linux-amd64-wheels.yml \
      -P self-hosted=-self-hosted \
      -P macOS=-self-hosted -P Linux=-self-hosted \
      -P X64=-self-hosted -P ARM64=-self-hosted \
      --input name=psutil
```

You can vary the `--input name=` argument to select which package to build or to build all.

On Linux you will need Docker or Podman.
If you're using Podman, make sure you are running the system service (e.g.
`podman system service -t 0` to run it in a shell), have symlinked or aliased `docker` to `podman`, and prepared you env with `export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock` to allow `act` to pick up where podman is listening.
