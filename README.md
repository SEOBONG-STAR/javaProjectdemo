# Crypto Portfolio Tracker

A Java portfolio-tracking project organized around separate application, model, service, strategy, observer, exception, and utility packages.

## Project goals

- Represent crypto assets and portfolio transactions using Java classes.
- Keep business logic in service classes rather than mixing it into the entry point.
- Support reusable valuation strategies and reporting.
- Work with CSV-based input/output for a lightweight local workflow.

## Technology

- Java (use a JDK compatible with the language features in the source)
- CSV files for local sample data
- IDE project metadata is included for convenient import

## Run

Use JDK 17 or newer. From the repository root, compile and run with:\n\n```bash\nmkdir -p build/classes\nfind src -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d build/classes\njava -cp build/classes com.crypto.app.App\n```\n\nRun the application from the repository root so the sample CSV files under `src/com/crypto/app/` are found. You can also import the repository into IntelliJ IDEA or Eclipse as an existing Java project and set the working directory to the repository root.

The repository currently contains IDE-generated metadata and compiled output folders. Prefer rebuilding compiled classes from source rather than relying on committed `bin/` artifacts.

## Architecture

The source tree separates responsibilities into packages such as:

- `app` — application entry point
- `model` — portfolio and transaction domain objects
- `service` — portfolio operations and calculations
- `strategy` — valuation strategy abstractions
- `observer` — change notifications
- `exception` — domain-specific errors
- `util` — CSV and supporting utilities

## Data and limitations

Use sample or synthetic transaction data. This project is a local portfolio calculator, not a trading platform or financial advice tool. Prices and profit/loss values are only as reliable as the input data.

## Next improvements

- Add a reproducible build with Maven or Gradle.
- Add unit tests for transaction validation, weighted-average cost, and realized/unrealized P&L.
- Validate malformed CSV rows and report actionable errors.
- Add sample data and documented expected outputs.
- Exclude generated IDE/build artifacts from version control.

## License

No license is currently specified. Contact the repository owner before reuse or redistribution.