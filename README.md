# ErCup - Simple Java Text Editor

![ErCup Logo](src/main/resources/icon.png)

ErCup is a straightforward text editor built in Java, offering a minimalistic interface for quick and efficient text editing.

## Integration with "Open with" Option

### Batch File Option

- Create a batch (.bat) file and place it in the same directory as ErCup.jar.
- Example batch code:

    ```batch
    @echo off
    pushd "%~dp0" || exit /B
    start javaw -jar "ErCup.jar" %1
    popd
    ```

### Executable File Option

- Utilize tools like Launch4j to wrap ErCup.jar into an executable (.exe) file.
- Associate the generated executable file with the "Open with" option.

Choose the method that best fits your preference, and enhance your text editing experience with ErCup!
