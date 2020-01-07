# Hardcore

[![Build Status](https://img.shields.io/github/workflow/status/Carlgo11/hardcore/Java%20CI?style=for-the-badge)](https://github.com/Carlgo11/hardcore/actions)
[![License](https://img.shields.io/badge/LICENSE-CC%20BY--NC%204.0-ee5b32?style=for-the-badge)](https://github.com/Carlgo11/hardcore/blob/master/LICENSE.md)
![Bukkit](https://img.shields.io/badge/bukkit-v1.15.1-ab7b53?style=for-the-badge)
## What is this?

Hardcore is a ultra-hardcore Minecraft plugin (Bukkit based). This is a replacement for an old outdated plugin I had a few years ago.

I decided to completely redesign the plugin from scratch and publish the source code in the process.

## Can I use the code?

Yes, this project is licensed under CC Attribution-NonCommercial 4.0 International Public License. For more information on this license, see [creativecommons.org](https://creativecommons.org/licenses/by-nc/4.0/legalcode), [tldrlegal.com](https://tldrlegal.com/license/creative-commons-attribution-noncommercial-4.0-international-%28cc-by-nc-4.0%29#summary) or [LICENSE](LICENSE.md).

For a short summery,

- You may run my code.
- You may make changes to my code as long as you give me credit for the contributions I have made.
- You may **not** claim that my code is your own or use any other license than CC Attribution-NonCommercial 4.0 International Public License.
- You may **not** make any economic profit on my code.

## Requirements

If you want to run the plugin you will need these things:

- Java 8
- A Spigot or Bukkit server
- Some knowledge on how Minecraft servers work and how to edit configs.

# Running the code

To run a local server and test/run the code follow these instructions:

1. Download a Spigot or Bukkit build.
2. Download the project. `git clone https://github.com/Carlgo11/hardcore.git`
3. Make a directory to run the server in. `mkdir server`
4. Copy the startup script and the scripts folder. `cp {scripts} server`
5. Go to the spigot directory and agree to the Minecraft EULA. `cd server; echo "eula=true" > eula.txt`
6. Start the server `./startup_script.sh`. This will download the latest Hardcore jar from GitHub.

_Note: You can build the Hardcore.jar and just place it in the `plugins` folder of an existing Minecraft server. If you choose to do so, skip the steps above._
