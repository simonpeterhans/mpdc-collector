# mpdc-collector

Prototype of a multi-platform data collection system to collect social media data from various (academic) APIs, suitable for long-running/continuous queries that can be recovered upon interrupt.

The CLI is, as of now, not yet implemented, and the usage of the Angular front end at [mpdc-ui](https://github.com/simonpeterhans/mpdc-ui) is recommended instead.

**Note:** You still need API access keys to use this system! The setup is meant to be local on a per-research-group or per-researcher basis.

##### APIs:

- Twitter Academic v2 (tweet search/stream, 1% stream)
- CrowdTangle Academic (account list search/stream)

##### Requirements:

- Java 17
- API keys for the above APIs if you plan to use the system

##### Deploying the REST API:

1. Run `./scripts/run_db.sh your_password` to create a docker container for Postgres (you can also set up a Postgres instance yourself).
2. Run `./gradlew clean build` in the root directory
3. Navigate to `collector-rest/build/distributions/` and extract the resulting build from the `.tar` or `.zip` file.
4. Make sure to adjust the config template and rename it to `config.json`, and copy it into the extracted directory.
5. Run `./bin/collector-rest` (with a valid `config.json` in the cwd) to start the API.

##### Notes:

- This is a prototype. Although it has successfully executed long-running queries and query recovery works fine, things might break.
- Documentation is scarce, and some things are solved more elegantly than others.
- If you want to make use of the vitrivr integration, make sure you check out the setup documentation of [Cineast](https://github.com/vitrivr/cineast), [vitrivr-ng](https://github.com/vitrivr/vitrivr-ng), and [Cottontail DB](https://github.com/vitrivr/cottontaildb).
- The Cineast version to be used is currently on a [fork](https://github.com/simonpeterhans/cineast), with a pull request to the main repo coming soon.
- Contributions are more than welcome!
