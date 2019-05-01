@echo off
git add -A
git commit -m %*
git push origin master
git push dev master