@echo off
echo Pushing code to GitHub...
cd /d "c:\Users\User\Downloads\FocusSphere\FocusSphere"
git add .
git commit -m "Update code for Render deployment"
git remote remove origin 2>nul
git remote add origin https://github.com/bhoivarun5-star/focusesphere.git
git branch -M main
git push -u origin main --force
echo.
echo Push complete! Press any key to close...
pause >nul
