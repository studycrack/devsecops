#!/bin/bash

sudo fallocate -l 4G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

sudo apt-get update
sudo apt-get install python3 python3-pip unzip -y

# semgrep 설치
python3 -m pip install semgrep
git clone https://github.com/digininja/DVWA.git

# gitleaks 설치
wget https://github.com/gitleaks/gitleaks/releases/download/v8.16.3/gitleaks_8.16.3_linux_x64.tar.gz
tar -xzf gitleaks_8.16.3_linux_x64.tar.gz
sudo mv gitleaks /usr/local/bin/
rm -rf gitleaks_8.16.3_linux_x64.tar.gz

# 노출 키 테스트 저장소
git clone https://github.com/trufflesecurity/test_keys

# ProjectDiscovery 도구 설치
wget https://github.com/projectdiscovery/nuclei/releases/download/v2.9.2/nuclei_2.9.2_linux_amd64.zip
wget https://github.com/projectdiscovery/naabu/releases/download/v2.1.5/naabu_2.1.5_linux_amd64.zip
wget https://github.com/projectdiscovery/subfinder/releases/download/v2.5.7/subfinder_2.5.7_linux_amd64.zip

unzip nuclei_2.9.2_linux_amd64.zip
sudo mv nuclei /usr/local/bin/

unzip naabu_2.1.5_linux_amd64.zip
sudo mv naabu /usr/local/bin/

unzip subfinder_2.5.7_linux_amd64.zip
sudo mv subfinder /usr/local/bin/

nuclei

# gitleaks 테스트 명령어 : gitleaks detect --source test_keys/ -v
# semgrep 테스트 명령어 : semgrep scan --config auto DVWA/


