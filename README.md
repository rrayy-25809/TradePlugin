![TradePlugin-render-v1-transparent-v3](https://user-images.githubusercontent.com/35135025/224560888-f808ca14-fda0-48ea-9b28-b515fa210585.png)
# Trado

**Trado is a powerful Minecraft plugin that enables players to trade items with ease and confidence. 
Its intuitive user interface and secure trading features make it the perfect solution for 
Minecraft enthusiasts of all skill levels. 
With Trado, you can experience the future of trading in Minecraft today!**

Scroll down to find the **installation instructions.**

## About

Ladies and gentlemen, we are thrilled to present Trado - the cutting-edge Java plugin that is changing the game when it comes to Minecraft item trading.

Trado offers a secure and user-friendly trading experience through an intuitive inventory GUI. With its innovative features, such as the ability to send trade requests with a simple right-click on another player, Trado has already gained a reputation as a must-have plugin for Minecraft servers.

One of the unique features of Trado is its multi-language support, making it accessible to users from all around the world. And with its easy setup, Trado can be up and running on your server in no time, allowing your players to start trading items safely and efficiently.

Trado's secure trading ensures that players can trade items with confidence, knowing that their items are safe and secure during trades. And with its intuitive design, Trado is accessible to all players, regardless of their level of experience.

But that's not all - imagine this: with just a right-click on another player, you can initiate a trade window and see exactly what they're offering. It's that simple! And when you're in the trading window, you can move your own items to the left slots and see what your opponent is offering on the right.

Whether you're a seasoned Minecraft veteran or just starting out, Trado makes item trading easy and hassle-free. So why wait? Download Trado today and join the countless Minecraft users who have already discovered the benefits of this game-changing plugin. Trado is sure to become a staple for Minecraft servers everywhere!

Thank you for your time, and happy trading with Trado!


## Usage

Trado offers two methods of initiating a trade - through the `right-click` feature and the `/trade <Name>` command.

To initiate a trade through the right-click feature, simply right-click on the player you want to trade with. This will open up a trading window where you can see what items the other player is offering. To offer your own items, simply move them to the left side of the trading window. Once both players have added their items, they can click on the "Trade" button to complete the trade.

To initiate a trade through the `/trade <Name>` command, type the command followed by the name of the player you want to trade with. This will send a trade request to the other player. If the other player accepts, the trading window will open and you can proceed with the trade as described above.

Users can accept incoming trades by using the interactive "Accept" button in chat or by using the `/trade accept` command. Once the trade is complete, both players will receive their traded items and the trading window will close.

Overall, Trado makes item trading in Minecraft easier and more secure than ever before. Try it out today and experience hassle-free item trading on your server!


## Commands

`/trade <Name>` >  Send another player a trade request

`/trade accept` or `/trade accept <Name>` >  Accept another player's trade request

`/trade deny` or `/trade deny <Name>` >  Decline a trade request

`/trade author` >  Shows the plugin authors

`/trade download` >  Shows where you can download the plugin safely

`/trade version` >  Shows the current plugin version

`/trade toggle` >  Toggles, if you need specific permission to trade


## Permissions

`trade.trade` > Allow basic trading by command

`trade.version` > Allow showing the current plugin version

`trade.*` > Allow everything (including reload)


## Config

`time_until_trade_request_gets_invalid` > Set the time in minutes, how long a trade request needs, until it gets canceled automatically

`language_version` > Set to `en_us` by default. Another language, integrated by default, is `de`

`use_without_permission` > Let users use the trades without explicitly needing permissions set with a permissions system

`enable_trade_by_right_click_player` > Enables the trade-by-right-click feature when set to `true` (default)

`max_distance_for_using_trade_command` > Specify a maximum distance for the /trade <Name> command. Values lower than 1 disable the limitation (default is -1)


## Language support

Trado offers multi-language support, allowing server administrators to customize the plugin's text to their preferred language. Here's how you can add your own translations:

1. Navigate to the `/plugins/TradePlugin/languages` directory on your server.
2. Locate the `en_us.yml` file and make a copy of it.
3. Rename the copy to your desired language code, e.g. `es_es.yml` for Spanish.
4. Open the file in a text editor and replace the English text with your translated text. Be sure to keep the formatting and syntax of the file intact.
5. **Save the file** and exit the text editor.
6. Open the `config.yml` file located in the `/plugins/TradePlugin/` directory.
7. Locate the `language_version` parameter and update it to the version of your language file, e.g. `es_es` for Spanish.
8. **Save the `config.yml`** file and exit.
9. **Restart** your Minecraft server for the changes to take effect.
10. And that's it! Trado will now use your custom translations whenever the plugin is loaded on your server. If you need to update your translations in the future, simply edit the language file and update the language_version parameter in the config.yml file.


## Install Trado

Introducing Trado, the specially crafted Minecraft plugin for secure and seamless trading. The Trado team has designed the installation process to be as easy and intuitive as possible, so you can start trading in no time!

**To install Trado, simply follow these steps:**

1. **Go to** the Trado GitHub Releases page: https://github.com/Robby3St/TradePlugin/releases.
2. Scroll down to the **latest release** and **download** the Trado.jar file.
3. **Place** the Trado.jar file into the **plugins folder** of your PaperMC server.
4. **Start or restart** your PaperMC server.

Trado should now be installed and ready to use.


That's it! With just a few simple steps, you can have Trado up and running on your PaperMC server.
With Trado's specially designed structure, the plugin works out of the box without any additional configuration needed. 
Secure trading has never been easier. 

Happy trading!
