package jp.vmi.selenium.runner.model.side;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.GsonBuilder;

import jp.vmi.selenium.runner.model.IProject;

/**
 * parsed side format data.
 */
public class SideProject implements IProject<SideSuite, SideTest, SideCommand> {

    private final String filename;
    private final String name;
    private final String id;
    private final String url;
    private final List<SideSuite> suites;
    private final Map<String, SideSuite> suiteMap = new HashMap<>();
    private final Map<String, SideTest> testMap = new HashMap<>();

    SideProject(String filename, SideFile sideFile) {
        this(filename, sideFile, null, null);
    }

    SideProject(String filename, SideFile sideFile, String suiteFilter, String caseFilter) {
        this.filename = filename;
        this.name = sideFile.getName();
        this.id = sideFile.getId();
        this.url = sideFile.getUrl();

        /*
         * filter test cases
         */
        sideFile.getTests().stream().filter(test -> {
            if (caseFilter != null) {
                String[] caseNames = caseFilter.split("\\|");
                return ArrayUtils.contains(caseNames, test.getName());
            }
            return true;
        }).forEach(test -> testMap.put(test.getId(), test));
        /*
         * filter test suites
         */
        this.suites = sideFile.getSuites().stream().filter(suite -> {
            if (suiteFilter != null) {
                String[] suiteNames = suiteFilter.split("\\|");
                if (!ArrayUtils.contains(suiteNames, suite.getName())) { // not include in suite filter
                    return false;
                }
            }
            return suite.getTests().stream().map(SideTest::getId).anyMatch(testId -> {
                return testMap.get(testId) != null;
            });
        }).collect(Collectors.toList());

        suites.stream().forEach(suite -> {
            ListIterator<SideTest> iter = suite.getTests().listIterator();
            while (iter.hasNext()) {
                SideTest unresolved = iter.next();
                SideTest resolved = testMap.get(unresolved.getId());
                iter.set(resolved);
            }
            suiteMap.put(suite.getId(), suite);
        });
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<SideSuite> getSuites() {
        return suites;
    }

    @Override
    public Map<String, SideSuite> getSuiteMap() {
        return suiteMap;
    }

    @Override
    public Map<String, SideTest> getTestMap() {
        return testMap;
    }

    /**
     * Serialize to JSON.
     *
     * @param object object.
     * @return JSON string.
     */
    public static String toJson(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }
}
