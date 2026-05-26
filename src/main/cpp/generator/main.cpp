#include <iostream>
#include <fstream>
#include <filesystem>
#include <string>
#include <sstream>

#include "json.hpp"

using namespace std;
using json = nlohmann::json;
namespace fs = filesystem;

static string buildIconName(const string& socketName) {
    string result = socketName;

    const string suffix = "_power";

    if (result.size() >= suffix.size()) {
        if (result.substr(result.size() - suffix.size()) == suffix) {
            result.erase(result.size() - suffix.size());
        }
    }

    return result;
}

static string toDisplayName(const string& id) {
    stringstream stream(id);
    string part;
    string result;

    while (getline(stream, part, '_')) {

        if (!result.empty()) {
            result += " ";
        }

        if (!part.empty()) {
            part[0] = static_cast<char>(toupper(part[0]));

            for (size_t i = 1; i < part.size(); i++) {
                part[i] = static_cast<char>(tolower(part[i]));
            }

            result += part;
        }
    }

    return result;
}

int main() {
    const fs::path inputPath = "input.json";
    const fs::path socketDir = "socket_types";
    const fs::path langDir = "lang";

    const fs::path langFilePath = langDir / "en_us.json";

    if (!fs::exists(inputPath)) {
        cerr << "input.json not found!" << endl;
        return 1;
    }

    fs::create_directories(socketDir);
    fs::create_directories(langDir);

    ifstream inputFile(inputPath);

    json inputJson;
    inputFile >> inputJson;
    json langJson = json::object();

    for (const auto& [category, socketArray] : inputJson.items()) {
        if (!socketArray.is_array()) {
            continue;
        }

        for (const auto& socketValue : socketArray) {
            if (!socketValue.is_string()) {
                continue;
            }

            string socketName = socketValue.get<string>();

            json socketJson;

            socketJson["category"] = category;
            socketJson["display_name"] = "socket." + socketName;

            string iconName = buildIconName(socketName);
            socketJson["icon"] ="rtstructures:textures/gui/socket/" +iconName +".png";

            fs::path outputPath =socketDir / (socketName + ".json");
            ofstream outputFile(outputPath);
            outputFile << socketJson.dump(4);

            cout << "Generated socket: "<< outputPath << endl;

            langJson["socket." + socketName] =toDisplayName(socketName);
        }
    }

    ofstream langFile(langFilePath);
    langFile << langJson.dump(4);

    cout << "Generated lang file: " << langFilePath << endl;
    cout << "Done!" << endl;

    return 0;
}