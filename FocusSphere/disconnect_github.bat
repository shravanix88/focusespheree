@echo off
echo Removing connection to GitHub repository...

cd /d "c:\Users\User\Downloads\FocusSphere"
git remote remove origin 2>nul

cd /d "c:\Users\User\Downloads\FocusSphere\FocusSphere"
git remote remove origin 2>nul

echo.
echo Connection removed successfully! The code is no longer linked to the GitHub repository.
echo Press any key to close...
pause >nul
